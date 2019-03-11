package presentation.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.telephony.CellInfoGsm
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.github.keyrillanskiy.cloather.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import domain.models.exceptions.UiException
import domain.models.responses.Thing
import domain.models.values.Gender
import domain.models.values.Language
import domain.models.values.defineLanguage
import extensions.toast
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.common.Failure
import presentation.common.FiniteState
import presentation.common.Loading
import presentation.common.Success
import presentation.screens.auth.AuthActivity
import presentation.screens.gender.GenderActivity
import presentation.screens.intro.IntroActivity
import presentation.screens.settings.SettingsActivity
import presentation.screens.wardrobe.WardrobeActivity
import presentation.share.ErrorDialog
import timber.log.Timber
import utils.NetUtils
import utils.PermissionUtils
import utils.serverBaseUrl
import kotlin.properties.Delegates


/**
 * Главный экран приложения
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 16:28.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var viewHolder: MainViewHolder
    private val disposables = CompositeDisposable()
    private var currentState by Delegates.observable<FiniteState<Event>>(
        WithoutLocationPermissionState(),
        onChange = { _, old, new -> handleStateChange(new, old) }
    )
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isFirstLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar) // потому что по умолчанию стоит тема для splash
        super.onCreate(savedInstanceState)

        // routing
        when {
            viewModel.isFirstLaunch() -> {
                IntroActivity.launch(this)
                finish()
            }
            !viewModel.isUserAuthorized() -> {
                AuthActivity.launch(this)
                finish()
            }
            viewModel.isGenderUndefined() -> {
                GenderActivity.launch(this)
                finish()
            }
            else -> {
                isFirstLaunch = savedInstanceState == null
                initMainScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == RC_CHECK_SETTINGS) {
                currentState = if (resultCode == Activity.RESULT_OK) {
                    currentState.getNextState(Event.LocationEnabled)
                } else {
                    currentState.getNextState(Event.LocationDisabled)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        currentState = if ((requestCode == RC_LOCATION_PERMISSION
                    && grantResults.isNotEmpty()
                    && grantResults.all { it == PackageManager.PERMISSION_GRANTED })
        ) {
            currentState.getNextState(Event.LocationPermissionGranted)
        } else {
            currentState.getNextState(Event.LocationPermissionRejected)
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun handleStateChange(newState: FiniteState<Event>, oldState: FiniteState<Event>) {
        when (newState) {
            is WithoutLocationPermissionState -> currentState = currentState.getNextState(
                Event.RequestLocationPermission
            )
//            is WithoutLocationPermissionState -> currentState = currentState.getNextState(
//                Event.RequestLocationPermission
//            )
            is LocationPermissionRequestedState -> showLocationPermissionReasonDialog()
            is WithLocationPermissionState -> currentState = currentState.getNextState(Event.LocationEnabled)
//            is WithLocationPermissionState -> enableLocationIfNeed()
            is LocationEnabledState -> checkInternet()
            is ReadyToFetchDataState -> onReadyToFetch()
            is LoadingGeolocationState -> fetchLocationWithoutGPS()
            is UpdatingDataState -> viewModel.fetchWeatherAndWhatToWear(newState.latitude, newState.longitude)
        }
    }

    private fun initMainScreen() {
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewHolder = MainViewHolder(mainContent).setup {
            disableRefreshing()

            onRefresh = { currentState = currentState.getNextState(Event.FetchLocation) }
            onWardrobeClick = { WardrobeActivity.launch(this@MainActivity) }
            onSettingsClick = { SettingsActivity.launch(this@MainActivity) }
        }

        viewModel.observeData()

        // first action with state machine
        currentState = currentState.getNextState(Event.RequestLocationPermission)
    }

    private fun MainViewModel.observeData() {
        locationLiveData.observe(this@MainActivity, Observer { response ->
            when (response) {
                is Loading -> viewHolder.showRefreshing()
                is Success -> {
                    val latitude = response.value.position.latitude
                    val longitude = response.value.position.longitude
                    currentState = currentState.getNextState(Event.FetchData(latitude, longitude))
                }
                is Failure -> {
                    viewHolder.hideRefreshing()
                    handleError(UiException.FetchLocationException(response.error))
                }
            }
        })

        weatherAndWhatToWearLiveData.observe(this@MainActivity, Observer { response ->
            when (response) {
                is Loading -> viewHolder.showRefreshing()
                is Success -> {
                    val weather = response.value.first
                    textTextView.text =
                            "${weather.city}\n ${weather.currentWeather.type} ${weather.currentWeather.temperature}"
                    showClothes(response.value.second)
                    currentState = currentState.getNextState(Event.DataFetched)
                }
                is Failure -> {
                    viewHolder.hideRefreshing()
                    handleError(UiException.FetchDataException(response.error))
                }
            }
        })
    }

    private fun onReadyToFetch() {
        viewHolder.enableRefreshing()
        if (isFirstLaunch) {
            isFirstLaunch = false
            currentState = currentState.getNextState(Event.FetchLocation)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationWithoutGPS() {
        val language = getSystemLanguage()

        val telephony = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephony.phoneType == TelephonyManager.PHONE_TYPE_GSM) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val cellInfo = telephony.allCellInfo?.firstOrNull { it is CellInfoGsm } as? CellInfoGsm
                if (cellInfo == null) {
                    viewModel.fetchLocation(language)
                } else {
                    val cellIdentity = cellInfo.cellIdentity
                    viewModel.fetchLocation(
                        language, cellIdentity.cid, cellIdentity.lac, cellIdentity.mcc, cellIdentity.mnc
                    )
                }
            } else {
                val cellInfo = telephony.cellLocation as? GsmCellLocation
                if (cellInfo == null) {
                    viewModel.fetchLocation(language)
                } else {
                    viewModel.fetchLocation(language, cellInfo.cid, cellInfo.lac, telephony.simOperator)
                }
            }
        } else {
            viewModel.fetchLocation(language)
        }

    }

    //TODO refactor
    private fun showClothes(clothes: List<Thing>) {
        Single.fromCallable {
            val clothesDrawables = mutableListOf<Drawable>()

            val imageWidth = clothesImageView.width
            val imageHeight = clothesImageView.height

            val sortedClothes = clothes.sortedBy { it.priority }

            sortedClothes.filter { it.modelImages != null }
                .forEach { thing ->
                    val gender = viewModel.getUserGender()
                    val imageUrl = when (gender) {
                        Gender.MALE -> serverBaseUrl + thing.modelImages?.manImageUrl
                        Gender.FEMALE -> serverBaseUrl + thing.modelImages?.womanImageUrl
                        Gender.UNDEFINED -> throw IllegalArgumentException("Inappropriate gender: $gender")
                    }

                    if (!imageUrl.contains("/things/null.png")) {
                        val drawable = Glide.with(this)
                            .asDrawable()
                            .load(imageUrl)
                            .submit(imageWidth, imageHeight)
                            .get()

                        clothesDrawables.add(drawable)
                    }
                }
            clothesDrawables
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ clothesDrawables ->
                val clothesDrawable = LayerDrawable(clothesDrawables.toTypedArray())
                clothesImageView.setImageDrawable(clothesDrawable)

                viewHolder.hideRefreshing()
            }, {
                Timber.w(it)
                viewHolder.hideRefreshing()
            })
            .addTo(disposables)
    }

    private fun getSystemLanguage(): Language {
        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }

        return defineLanguage(currentLocale.language)
    }

    private fun showLocationPermissionReasonDialog() {
        if (PermissionUtils.isLocationPermissionGranted(this)) {
            currentState = currentState.getNextState(Event.LocationPermissionGranted)
        } else {
            supportFragmentManager.findFragmentByTag(RequestLocationDialog.TAG) as? RequestLocationDialog
                ?: RequestLocationDialog()
                    .also {
                        it.onPermitClick = { PermissionUtils.requestLocationPermission(this, RC_LOCATION_PERMISSION) }
                        it.onCancelClick = {
                            currentState = currentState.getNextState(Event.LocationPermissionRejected)
                        }
                        it.showNow(supportFragmentManager, RequestLocationDialog.TAG)
                    }
        }
    }

    private fun showLocationError() {
        val title = getString(R.string.location_error)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message, onRetryClick = {
            currentState = currentState.getNextState(Event.FetchLocation)
        })
    }

    private fun showInternetDisabledError() {
        val title = getString(R.string.fetching_data_interner_error_title)
        val message = getString(R.string.internet_connection_error_message)

        val changeStateLambda: () -> Unit = { currentState = currentState.getNextState(Event.InternetDisabled) }

        showErrorDialog(title, message, onOkClick = changeStateLambda, onRetryClick = changeStateLambda)
    }

    private fun showFetchDataError() {
        val title = getString(R.string.fetching_data_interner_error_title)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message, onRetryClick = {
            currentState = currentState.getNextState(Event.FetchLocation)
        })
    }

    private fun showUnknownError() {
        val title = getString(R.string.fetching_data_interner_error_title)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message)
    }

    private fun showErrorDialog(
        title: String? = null, message: String? = null,
        onOkClick: (() -> Unit)? = null, onRetryClick: (() -> Unit)? = null
    ) {
        supportFragmentManager.findFragmentByTag(ErrorDialog.TAG) as? ErrorDialog
            ?: ErrorDialog.newInstance(title, message)
                .also { dialog ->
                    onOkClick?.let { dialog.onOkClick = { it.invoke() } }
                    onRetryClick?.let { dialog.onRetryClick = { it.invoke() } }
                    dialog.showNow(supportFragmentManager, ErrorDialog.TAG)
                }
    }

    private fun enableLocationIfNeed() {
        checkLocation(
            onEnabled = { currentState = currentState.getNextState(Event.LocationEnabled) },
            onDisabled = { exception ->
                if (exception is ResolvableApiException) {
                    exception.startResolutionForResult(this@MainActivity, RC_CHECK_SETTINGS)
                } else {
                    showLocationError()
                }
            }
        )
    }

    private fun checkLocation(onEnabled: () -> Unit, onDisabled: (Exception) -> Unit) {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationRequest.create().apply {
                interval = 5 * 1000
                fastestInterval = 2 * 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            })
            .build()

        val settingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(settingsRequest)

        task.apply {
            addOnSuccessListener { onEnabled.invoke() }
            addOnFailureListener { exception -> onDisabled.invoke(exception) }
        }
    }

    private fun checkInternet() {
        if (NetUtils.hasNetConnection()) {
            currentState = currentState.getNextState(Event.InternetEnabled)
        } else {
            showInternetDisabledError()
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchGeolocation() {
        if (!PermissionUtils.isLocationPermissionGranted(this)) {
            currentState = currentState.getNextState(Event.LocationPermissionRemoved)
            return
        }
        checkLocation(
            onEnabled = {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            currentState = currentState.getNextState(
                                Event.FetchData(location.latitude, location.longitude)
                            )
                        } else {
                            handleError(UiException.FetchLocationException())
                        }
                    }
                    .addOnFailureListener {
                        handleError(UiException.FetchLocationException(throwable = it))
                    }
            },
            onDisabled = { currentState = currentState.getNextState(Event.LocationDisabled) }
        )

    }

    private fun handleError(exception: UiException) {
        when (exception) {
            is UiException.FetchLocationException -> showLocationError()
            is UiException.FetchDataException -> showFetchDataError()
            is UiException.UnknownException -> showUnknownError()
        }

        currentState = currentState.getNextState(Event.DataFetchErrorHandled)
    }

    companion object {
        private const val RC_LOCATION_PERMISSION = 1
        private const val RC_CHECK_SETTINGS = 2

        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}