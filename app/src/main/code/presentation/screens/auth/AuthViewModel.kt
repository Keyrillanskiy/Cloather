package presentation.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import domain.models.responses.TokenWrapper
import domain.models.responses.User
import io.reactivex.disposables.CompositeDisposable
import presentation.common.Response

/**
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:21.
 */
class AuthViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private val _authLiveData = MutableLiveData<Response<User>>()
    val authLiveData: LiveData<Response<User>>
        get() = _authLiveData

    fun authorize(token: String) {
        val tokenWrapper = TokenWrapper(token)
        //todo
    }

}