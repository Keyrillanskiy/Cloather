package presentation.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.preferences.Preferences
import data.repositories.interfaces.WardrobeRepository
import data.repositories.interfaces.WeatherRepository
import data.useCases.interfaces.LocationUseCase
import domain.models.responses.LocationResponse
import domain.models.responses.Thing
import domain.models.responses.WeatherResponse
import domain.models.values.Language
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import presentation.common.Response
import presentation.common.failure
import presentation.common.loading
import presentation.common.success
import timber.log.Timber
import utils.SchedulersFacade


/**
 * @author Keyrillanskiy
 * @since 19.01.2019, 15:29.
 */
class MainViewModel(
    private val preferences: Preferences,
    private val locationUseCase: LocationUseCase,
    private val weatherRepository: WeatherRepository,
    private val wardrobeRepository: WardrobeRepository,
    private val schedulers: SchedulersFacade
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _locationLiveData = MutableLiveData<Response<LocationResponse>>()
    val locationLiveData: LiveData<Response<LocationResponse>>
        get() = _locationLiveData

    private val _weatherAndWhatToWearLiveData = MutableLiveData<Response<Pair<WeatherResponse, List<Thing>>>>()
    val weatherAndWhatToWearLiveData: LiveData<Response<Pair<WeatherResponse, List<Thing>>>>
        get() = _weatherAndWhatToWearLiveData

    fun isFirstLaunch() = preferences.isFirstLaunch

    fun isUserAuthorized() = preferences.isUserAuthorized()

    fun isGenderUndefined() = preferences.isGenderUndefined()

    fun getUserGender() = preferences.gender

    fun fetchLocation(
        language: Language,
        cellId: Int? = null,
        lac: Int? = null,
        countryCode: Int? = null,
        operatorCode: Int? = null
    ) {
        locationUseCase.fetchLocation(language, cellId, lac, countryCode, operatorCode)
            .observeLocation()
    }

    fun fetchLocation(language: Language, cellId: Int? = null, lac: Int? = null, simOperator: String? = null) {
        locationUseCase.fetchLocation(language, cellId, lac, simOperator)
            .observeLocation()
    }

    private fun Single<LocationResponse>.observeLocation() {
        subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _locationLiveData.value = Response.loading() }
            .subscribe(
                { _locationLiveData.value = Response.success(it) },
                {
                    Timber.w(it)
                    _locationLiveData.value = Response.failure(it)
                }
            )
            .addTo(disposables)
    }

    fun fetchWeatherAndWhatToWear(latitude: Double, longitude: Double) {
        Single.zip(
            weatherRepository.fetchWeather(latitude, longitude),
            wardrobeRepository.fetchWhatToWear(latitude, longitude, preferences.token!!),
            BiFunction { weather: WeatherResponse, whatToWear: List<Thing> -> weather to whatToWear }
        )
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _weatherAndWhatToWearLiveData.value = Response.loading() }
            .subscribe(
                { _weatherAndWhatToWearLiveData.value = Response.success(it) },
                {
                    Timber.w(it)
                    _weatherAndWhatToWearLiveData.value = Response.failure(it)
                }
            )
            .addTo(disposables)
    }

}