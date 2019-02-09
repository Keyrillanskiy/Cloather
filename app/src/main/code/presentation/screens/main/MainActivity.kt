package presentation.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.CellInfoGsm
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.keyrillanskiy.cloather.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import domain.models.values.Language
import domain.models.values.defineLanguage
import extensions.toast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.common.Failure
import presentation.common.FiniteState
import presentation.common.Loading
import presentation.common.Success
import presentation.screens.auth.AuthActivity
import presentation.screens.gender.GenderActivity
import presentation.screens.intro.IntroActivity
import presentation.share.ErrorDialog
import timber.log.Timber
import utils.NetUtils
import utils.PermissionUtils
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
    private var currentState by Delegates.observable<FiniteState<Event>>(
        WithoutLocationPermissionState(),
        onChange = { _, old, new -> handleStateChange(new, old) }
    )
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
                initMainScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == RC_CHECK_SETTINGS) {
                currentState = if (resultCode == Activity.RESULT_OK) {
                    currentState.performTransition(Event.LocationEnabled)
                } else {
                    currentState.performTransition(Event.LocationDisabled)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        currentState = if ((requestCode == RC_LOCATION_PERMISSION
                    && grantResults.isNotEmpty()
                    && grantResults.all { it == PackageManager.PERMISSION_GRANTED })
        ) {
            currentState.performTransition(Event.LocationPermissionGranted)
        } else {
            currentState.performTransition(Event.LocationPermissionRejected)
        }
    }

    private fun handleStateChange(newState: FiniteState<Event>, oldState: FiniteState<Event>) {
        when (newState) {
            is WithoutLocationPermissionState -> currentState = currentState.performTransition(
                Event.RequestLocationPermission
            )
//            is WithoutLocationPermissionState -> currentState = currentState.performTransition(
//                Event.RequestLocationPermission
//            )
            is LocationPermissionRequestedState -> currentState = currentState.performTransition(Event.LocationPermissionGranted)
//            is LocationPermissionRequestedState -> showLocationPermissionReasonDialog()
            is WithLocationPermissionState -> currentState = currentState.performTransition(Event.LocationEnabled)
//            is WithLocationPermissionState -> enableLocationIfNeed()
            is LocationEnabledState -> checkInternet()
            is ReadyToFetchDataState -> currentState = currentState.performTransition(Event.FetchLocation)
            is LoadingGeolocationState -> fetchLocationWithoutGPS()
            is UpdatingDataState -> onUpdatingData(newState.latitude, newState.longitude)
            is DataFetchErrorState -> handleError(newState.throwable)
        }
    }

    private fun initMainScreen() {
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        viewHolder = MainViewHolder(mainContent).setup {
            onWardrobeClick = { toast("Not implemented") }
            onSettingsClick = { toast("Not implemented") }
        }

        viewModel.observeData()

        // first action with state machine
        currentState = currentState.performTransition(Event.RequestLocationPermission)
    }

    private fun MainViewModel.observeData() {
        locationLiveData.observe(this@MainActivity, Observer { response ->
            when (response) {
                is Loading -> {
                    /* nothing */
                }
                is Success -> {
                    Timber.d(response.toString())
                    toast("${response.value.position.latitude} ${response.value.position.longitude}")
                }
                is Failure -> showLocationError()
            }
        })
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
            currentState = currentState.performTransition(Event.LocationPermissionGranted)
        } else {
            supportFragmentManager.findFragmentByTag(RequestLocationDialog.TAG) as? RequestLocationDialog
                ?: RequestLocationDialog()
                    .also {
                        it.onPermitClick = { PermissionUtils.requestLocationPermission(this, RC_LOCATION_PERMISSION) }
                        it.onCancelClick = {
                            currentState = currentState.performTransition(Event.LocationPermissionRejected)
                        }
                        it.showNow(supportFragmentManager, RequestLocationDialog.TAG)
                    }
        }
    }

    private fun showLocationError() {
        val title = getString(R.string.location_error)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message)
    }

    private fun showInternetDisabledError() {
        val title = getString(R.string.fetching_data_interner_error_title)
        val message = getString(R.string.internet_connection_error_message)

        val changeStateLambda: () -> Unit = { currentState = currentState.performTransition(Event.LocationDisabled) }

        showErrorDialog(title, message, onOkClick = changeStateLambda, onRetryClick = changeStateLambda)
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
            onEnabled = { currentState = currentState.performTransition(Event.LocationEnabled) },
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
            currentState = currentState.performTransition(Event.InternetEnabled)
        } else {
            showInternetDisabledError()
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchGeolocation() {
        if (!PermissionUtils.isLocationPermissionGranted(this)) {
            currentState = currentState.performTransition(Event.LocationPermissionRemoved)
            return
        }
        checkLocation(
            onEnabled = {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            currentState = currentState.performTransition(
                                Event.FetchData(location.latitude, location.longitude)
                            )
                        } else {
                            currentState = currentState.performTransition(Event.FetchLocationError())
                        }
                    }
                    .addOnFailureListener {
                        currentState = currentState.performTransition(Event.FetchLocationError(it))
                    }
            },
            onDisabled = { currentState = currentState.performTransition(Event.LocationDisabled) }
        )

    }

    private fun onUpdatingData(latitude: Double, longitude: Double) {
        toast("$latitude $longitude")
        //currentState = currentState.performTransition(Event.DataFetched)
    }

    private fun handleError(throwable: Throwable?) {
        Timber.w(throwable)
        toast("error: ${throwable?.localizedMessage}")
    }

    companion object {
        private const val RC_LOCATION_PERMISSION = 1
        private const val RC_CHECK_SETTINGS = 2

        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}