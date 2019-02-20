package presentation.screens.auth

import android.accounts.Account
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.GoogleAuthUtil
import data.preferences.Preferences
import data.useCases.interfaces.UserUseCase
import domain.models.entities.User
import domain.models.responses.TokenWrapper
import domain.models.responses.UserResponse
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import presentation.common.*
import timber.log.Timber
import utils.SchedulersFacade

/**
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:21.
 */
class AuthViewModel(
    private val userUseCase: UserUseCase,
    private val preferences: Preferences,
    private val schedulers: SchedulersFacade
) : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private val _authLiveData = SingleLiveData<Response<User>>()
    val authLiveData: LiveData<Response<User>>
        get() = _authLiveData

    fun authorize(token: String) {
        val tokenWrapper = TokenWrapper(token)
        userUseCase.authorize(tokenWrapper)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _authLiveData.value = Response.loading() }
            .subscribe(
                { _authLiveData.value = Response.success(it) },
                {
                    Timber.w(it)
                    _authLiveData.value = Response.failure(it)
                }
            )
            .addTo(disposables)
    }

    fun cacheUser(user: User) = preferences.cacheUser(user)

    fun isUserAuthorized() = preferences.isUserAuthorized()

    fun isGenderUndefined() = preferences.isGenderUndefined()

}