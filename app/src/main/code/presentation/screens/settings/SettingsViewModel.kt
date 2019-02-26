package presentation.screens.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.preferences.Preferences
import data.useCases.interfaces.UserUseCase
import domain.models.entities.User
import domain.models.values.Gender
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import presentation.common.Response
import presentation.common.failure
import presentation.common.loading
import presentation.common.success
import timber.log.Timber
import utils.SchedulersFacade
import java.util.concurrent.TimeUnit

/**
 * @author Keyrillanskiy
 * @since 25.02.2019, 8:13.
 */
class SettingsViewModel(
    private val userUseCase: UserUseCase,
    private val preferences: Preferences,
    private val schedulers: SchedulersFacade
) :
    ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private val _userLiveData = MutableLiveData<Response<User>>()
    val userLiveData: LiveData<Response<User>>
        get() = _userLiveData

    private val _uploadSettingsLiveData = MutableLiveData<Response<Unit>>()
    val uploadSettingsLiveData: LiveData<Response<Unit>>
        get() = _uploadSettingsLiveData

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


}