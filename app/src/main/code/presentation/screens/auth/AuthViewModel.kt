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

    private val _tokenLiveData = SingleLiveData<Response<String>>()
    val tokenLiveData: LiveData<Response<String>>
        get() = _tokenLiveData

    private val _authLiveData = SingleLiveData<Response<User>>()
    val authLiveData: LiveData<Response<User>>
        get() = _authLiveData

    fun getGoogleToken(context: Context, accountName: String) {
        Single.fromCallable {
            GoogleAuthUtil.getToken(
                context,
                Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE),
                SCOPES
            )

        }.subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _tokenLiveData.value = Response.loading() }
            .subscribe(
                { _tokenLiveData.value = Response.success(it) },
                {
                    Timber.w(it)
                    _tokenLiveData.value = Response.failure(it)
                }
            ).addTo(disposables)
    }

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

    private companion object {
        private const val G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me"
        private const val USER_INFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile"
        private const val EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email"
        private const val SCOPES = "$G_PLUS_SCOPE $USER_INFO_SCOPE $EMAIL_SCOPE"
    }

}