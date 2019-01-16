package presentation.screens.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.common.AccountPicker
import com.jakewharton.rxbinding3.view.clicks
import domain.models.responses.SCOPES
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.share.ProgressDialog
import timber.log.Timber
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authButton.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { authorize() }
            .addTo(disposables)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_CANCELED && data != null) {
            val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            getGoogleToken(accountName)
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun authorize() {
        val chooseAccountIntent = AccountPicker.newChooseAccountIntent(
            null, null,
            arrayOf("com.google"),
            false, null, null, null, null
        )

        if (chooseAccountIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(chooseAccountIntent, RC_SIGN_IN)
        }
    }

    private fun getGoogleToken(accountName: String) {
        try {
            val googleToken = GoogleAuthUtil.getToken(
                this,
                Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE),
                SCOPES
            )
            Timber.d("googleToken = $googleToken")

            viewModel.authorize(googleToken)

        } catch (e: UserRecoverableAuthException) {
            startActivityForResult(e.intent, RC_SIGN_IN)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun showAuthProgress() {
        supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog
            ?: ProgressDialog.newInstance(getString(R.string.authorization))
                .also { it.showNow(supportFragmentManager, ProgressDialog.TAG) }
    }

    private fun hideAuthProgress() {
        (supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog)?.dismiss()
    }

    private companion object {
        private const val RC_SIGN_IN = 1
    }

}