package presentation.screens.gender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.preferences.Preferences
import data.useCases.interfaces.UserUseCase
import domain.models.values.Gender
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
 * @since 19.01.2019, 16:55.
 */
class GenderViewModel(
    private val preferences: Preferences,
    private val userUseCase: UserUseCase,
    private val schedulers: SchedulersFacade
) : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private val _genderLiveData = MutableLiveData<Response<Gender>>()
    val genderLiveData: LiveData<Response<Gender>>
        get() = _genderLiveData

    fun sendGender(gender: Gender) {
        val userId = preferences.fetchUser().id
        userUseCase.setGender(userId!!, gender)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _genderLiveData.value = Response.loading() }
            .subscribe(
                { _genderLiveData.value = Response.success(gender) },
                {
                    Timber.w(it)
                    _genderLiveData.value = Response.failure(it)
                }
            )
            .addTo(disposables)
    }

    fun saveGender(gender: Gender) {
        preferences.gender = gender
    }

    fun isGenderUndefined() = preferences.isGenderUndefined()

    fun isUserAuthorized() = preferences.isUserAuthorized()

}