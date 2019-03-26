package presentation.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.mappers.interfaces.WeatherMapper
import data.preferences.Preferences
import data.repositories.interfaces.WeatherRepository
import data.useCases.interfaces.LocationUseCase
import data.useCases.interfaces.UserUseCase
import data.useCases.interfaces.WardrobeUseCase
import domain.models.entities.User
import domain.models.responses.Category
import domain.models.responses.LocationResponse
import domain.models.responses.Thing
import domain.models.responses.WeatherResponse
import domain.models.values.Gender
import domain.models.values.Language
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import presentation.common.Response
import presentation.common.failure
import presentation.common.loading
import presentation.common.success
import presentation.screens.wardrobe.ThingItem
import presentation.screens.weather.WeatherCurrentItemData
import presentation.screens.weather.WeatherForecastItemData
import timber.log.Timber
import utils.SchedulersFacade

/**
 * @author Keyrillanskiy
 * @since 13.03.2019, 9:14.
 */
class MainViewModel(
    val preferences: Preferences,
    private val locationUseCase: LocationUseCase,
    private val weatherRepository: WeatherRepository,
    private val wardrobeUseCase: WardrobeUseCase,
    private val userUseCase: UserUseCase,
    private val mapper: WeatherMapper,
    private val schedulers: SchedulersFacade
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _locationLiveData = MutableLiveData<Response<LocationResponse>>()
    val locationLiveData: LiveData<Response<LocationResponse>>
        get() = _locationLiveData

    private val _weatherAndWhatToWearLiveData = MutableLiveData<Response<Pair<WeatherResponse, List<Thing>>>>()
    val weatherAndWhatToWearLiveData: LiveData<Response<Pair<WeatherResponse, List<Thing>>>>
        get() = _weatherAndWhatToWearLiveData

    private val _userLiveData = MutableLiveData<Response<User>>()
    val userLiveData: LiveData<Response<User>>
        get() = _userLiveData

    private val _uploadSettingsLiveData = MutableLiveData<Response<Unit>>()
    val uploadSettingsLiveData: LiveData<Response<Unit>>
        get() = _uploadSettingsLiveData

    private val _categoriesLiveData = MutableLiveData<Response<List<Category>>>()
    val categoriesLiveData: LiveData<Response<List<Category>>>
        get() = _categoriesLiveData

    private val _thingsLiveData = MutableLiveData<Response<Pair<List<Thing>, List<Thing>>>>()
    val thingsLiveData: LiveData<Response<Pair<List<Thing>, List<Thing>>>>
        get() = _thingsLiveData

    private val _thingUpdateLiveData = MutableLiveData<Response<Unit>>()
    val thingUpdateLiveData: LiveData<Response<Unit>>
        get() = _thingUpdateLiveData

    val gender: Gender
        get() = preferences.gender

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
            wardrobeUseCase.fetchWhatToWear(latitude, longitude, preferences.token!!),
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

    fun fetchUser() {
        userUseCase.fetchUser()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _userLiveData.value = Response.loading() }
            .subscribe(
                { _userLiveData.value = Response.success(it) },
                {
                    _userLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

    fun uploadSettings() {
        val user = preferences.fetchUser()
        val uid = user.id
        val gender = user.gender
        uid?.let { id ->
            userUseCase.setGender(id, gender)
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .doOnSubscribe { _uploadSettingsLiveData.value = Response.loading() }
                .subscribe(
                    { _uploadSettingsLiveData.value = Response.success(Unit) },
                    {
                        _uploadSettingsLiveData.value = Response.failure(it)
                        Timber.w(it)
                    }
                )
        }
    }

    fun saveGender(gender: Gender) {
        preferences.gender = gender
    }

    fun deleteUserData() = preferences.deleteUserData()

    fun fetchCategories(gender: Gender = preferences.gender) {
        wardrobeUseCase.fetchCaterories(gender)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _categoriesLiveData.value = Response.loading() }
            .subscribe(
                { _categoriesLiveData.value = Response.success(it) },
                {
                    _categoriesLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

    fun fetchThings(categoryId: String, userId: String? = preferences.uid) {
        Single.zip(
            wardrobeUseCase.fetchUsersWardrobe(userId),
            wardrobeUseCase.fetchThingsForWardrobeScreen(categoryId, preferences.gender),
            BiFunction { wardrobe: List<Thing>, thingsByCategory: List<Thing> -> wardrobe to thingsByCategory }
        )
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _thingsLiveData.value = Response.loading() }
            .subscribe(
                { _thingsLiveData.value = Response.success(it) },
                {
                    _thingsLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

    fun updateThing(thingItem: ThingItem, userId: String? = preferences.uid) {
        if (userId == null) {
            return
        }

        val completable = if (thingItem.isChecked) {
            wardrobeUseCase.addThingToWardrobe(userId, thingItem.id)
        } else {
            wardrobeUseCase.deleteThingFromWardrobe(userId, thingItem.id)
        }

        completable.subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _thingUpdateLiveData.value = Response.loading() }
            .subscribe(
                { _thingUpdateLiveData.value = Response.success(Unit) },
                {
                    _thingUpdateLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

    fun mapToCurrentWeatherAndForecast(
        weatherResponse: WeatherResponse,
        language: Language
    ): Pair<WeatherCurrentItemData, List<WeatherForecastItemData>> {
        val mappedCurrentWeather = mapper.toCurrentWeatherItemData(weatherResponse, language)
        val mappedForecast = weatherResponse.forecast.map { mapper.toForecastWeatherItemData(it) }
        return mappedCurrentWeather to mappedForecast
    }

}