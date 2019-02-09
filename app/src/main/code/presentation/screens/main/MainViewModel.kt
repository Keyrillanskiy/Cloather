package presentation.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.preferences.Preferences
import data.useCases.interfaces.LocationUseCase
import domain.models.responses.LocationResponse
import domain.models.values.Language
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
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
    private val schedulers: SchedulersFacade
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _locationLiveData = MutableLiveData<Response<LocationResponse>>()
    val locationLiveData: LiveData<Response<LocationResponse>>
        get() = _locationLiveData

    fun isFirstLaunch() = preferences.isFirstLaunch

    fun isUserAuthorized() = preferences.isUserAuthorized()

    fun isGenderUndefined() = preferences.isGenderUndefined()

    fun fetchLocation(
        language: Language,
        cellId: Int? = null,
        lac: Int? = null,
        countryCode: Int? = null,
        operatorCode: Int? = null
    ) {
        locationUseCase.fetchLocation(language, cellId, lac, countryCode, operatorCode)
            .observe()
    }

    fun fetchLocation(language: Language, cellId: Int? = null, lac: Int? = null, simOperator: String? = null) {
        locationUseCase.fetchLocation(language, cellId, lac, simOperator)
            .observe()
    }

    private fun Single<LocationResponse>.observe() {
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

}