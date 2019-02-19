package presentation.screens.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import androidx.lifecycle.Observer
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jakewharton.rxbinding3.view.clicks
import extensions.toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.common.Failure
import presentation.common.Loading
import presentation.common.Success
import presentation.screens.gender.GenderActivity
import presentation.screens.main.MainActivity
import presentation.share.ErrorDialog
import presentation.share.ProgressDialog
import timber.log.Timber
import utils.NetUtils
import utils.googleAuthClientId
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Экран авторизации (пока только через google)
 *
 * TODO: переписать авторизацию через google на более современный вариант
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 9:54.
 */
class AuthActivity : AppCompatActivity() {

    private val viewModel by viewModel<AuthViewModel>()
    private val disposables = CompositeDisposable()

    private var fragmentsToShow: LinkedList<FragmentToShow>? = null
    private var isCanShowFragments = true // если вызовется onSaveInstanceState, то нельзя отображать фрагменты

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.isUserAuthorized()) {
            if (viewModel.isGenderUndefined()) {
                GenderActivity.launch(this)
                finish()
            } else {
                MainActivity.launch(this)
                finish()
            }
        } else {
            initAuthScreen(savedInstanceState)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

        isCanShowFragments = true

        fragmentsToShow?.run {
            forEach { fragment ->
                when (fragment) {
                    FragmentToShow.Progress -> showAuthProgress()
                    is FragmentToShow.Error -> showErrorDialog(fragment.title, fragment.message)
                }
            }
            clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_CANCELED && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        isCanShowFragments = false
        outState?.putSerializable(KEY_FRAGMENTS_TO_SHOW, fragmentsToShow)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun initAuthScreen(savedInstanceState: Bundle?) {
        setContentView(com.github.keyrillanskiy.cloather.R.layout.activity_auth)

        fragmentsToShow = (savedInstanceState?.getSerializable(KEY_FRAGMENTS_TO_SHOW) as? LinkedList<FragmentToShow>)
                ?: LinkedList()

        authButton.clicks()
            .throttleFirst(800, TimeUnit.MILLISECONDS)
            .subscribe { authorize() }
            .addTo(disposables)

        viewModel.observeData()
    }

    private fun authorize() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleAuthClientId)
            .build()

        val signInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            //todo
            account.idToken?.let { toast(it) }
        } else {
            NetUtils.withNetConnection(
                onSuccess = { startActivityForResult(signInClient.signInIntent, RC_SIGN_IN) },
                onError = { showInternetAuthError() }
            )
        }

    }

    private fun handleSignInResult(signInTask: Task<GoogleSignInAccount>) {
        showAuthProgress()

        try {
            val account = signInTask.getResult(ApiException::class.java)
            account?.idToken?.let { toast(it) }
            hideAuthProgress() //todo remove
            //todo
            //viewModel.getGoogleToken(this, accountName)
        } catch (e: Exception) {
            Timber.w(e)
            showUnknownAuthError()
            hideAuthProgress()
        }

    }

    private fun showAuthProgress() {
        if (isCanShowFragments) {
            supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog
                ?: ProgressDialog.newInstance(getString(com.github.keyrillanskiy.cloather.R.string.authorization))
                    .also { it.showNow(supportFragmentManager, ProgressDialog.TAG) }
        } else {
            fragmentsToShow?.add(FragmentToShow.Progress)
        }

    }

    private fun hideAuthProgress() {
        (supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog)?.dismiss()
    }

    private fun showInternetAuthError() {
        val title = getString(com.github.keyrillanskiy.cloather.R.string.authorization_error_title)
        val message = getString(com.github.keyrillanskiy.cloather.R.string.internet_connection_error_message)

        showErrorDialog(title, message)
    }

    private fun showUnknownAuthError() {
        val title = getString(com.github.keyrillanskiy.cloather.R.string.authorization_error_title)
        val message = getString(com.github.keyrillanskiy.cloather.R.string.unknown_error)

        showErrorDialog(title, message)
    }

    private fun showErrorDialog(title: String? = null, message: String? = null) {
        if (isCanShowFragments) {
            supportFragmentManager.findFragmentByTag(ErrorDialog.TAG) as? ErrorDialog
                ?: ErrorDialog.newInstance(title, message)
                    .also {
                        it.onOkClick = { hideAuthProgress() }
                        it.onRetryClick = { authorize() }
                        it.showNow(supportFragmentManager, ErrorDialog.TAG)
                    }
        } else {
            fragmentsToShow?.add(FragmentToShow.Error(title, message))
        }

    }

    private fun AuthViewModel.observeData() {
        tokenLiveData.observe(this@AuthActivity, Observer { response ->
            when (response) {
                is Loading -> {
                    //nothing
                }
                is Success -> {
                    val googleToken = response.value
                    Timber.d("googleToken = $googleToken")
                    authorize(googleToken)
                }
                is Failure -> {
                    val error = response.error
                    if (error is UserRecoverableAuthException) {
                        startActivityForResult(error.intent, RC_SIGN_IN)
                    } else {
                        hideAuthProgress()
                        showUnknownAuthError()
                    }
                }
            }
        })

        authLiveData.observe(this@AuthActivity, Observer { response ->
            when (response) {
                is Loading -> {
                    //nothing
                }
                is Success -> {
                    cacheUser(response.value)
                    hideAuthProgress()
                    MainActivity.launch(this@AuthActivity)
                    finish()
                }
                is Failure -> {
                    hideAuthProgress()
                    showUnknownAuthError()
                }
            }
        })
    }

    // Для добавления в очередь отображения, если экран перешел в onSaveInstanceState
    private sealed class FragmentToShow {
        object Progress : FragmentToShow()
        data class Error(val title: String? = null, val message: String? = null) : FragmentToShow()
    }

    companion object {
        private const val RC_SIGN_IN = 1

        private const val KEY_FRAGMENTS_TO_SHOW = "com.github.keyrillanskiy.cloather.key.fragments_to_show"

        fun launch(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }

}