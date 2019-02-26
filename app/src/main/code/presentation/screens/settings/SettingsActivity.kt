package presentation.screens.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import androidx.lifecycle.Observer
import com.github.keyrillanskiy.cloather.R
import extensions.gone
import extensions.visible
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.common.Failure
import presentation.common.Loading
import presentation.common.Success
import presentation.screens.auth.AuthActivity
import presentation.share.ErrorDialog
import utils.NetUtils

/**
 * Экран настроек
 *
 * @author Keyrillanskiy
 * @since 22.02.2019, 10:10.
 */
class SettingsActivity : AppCompatActivity(), SettingsInteractor {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel.apply {
            uploadSettingsLiveData.observe(this@SettingsActivity, Observer { response ->
                when (response) {
                    is Loading -> {
                        /*nothing*/
                    }
                    is Success -> finish()
                    is Failure -> {
                        showUploadSettingsError()
                    }
                }
            })

            userLiveData.observe(this@SettingsActivity, Observer { response ->
                when (response) {
                    is Loading, is Success -> {
                        /*nothing*/
                    }
                    is Failure -> handleUserDataCorruption()
                }
            })
        }
    }

    override fun onBackPressed() {
        NetUtils.withNetConnection(
            onSuccess = {
                viewModel.uploadSettings()
            }, onError = {
                showUploadSettingsInternetError()
            })
    }

    override fun onLogOut() {
        logOut()
    }

    override fun onShowLoading() {
        progressBar.visible()
    }

    override fun onHideLoading() {
        progressBar.gone()
    }

    private fun showUploadSettingsError() {
        val title = getString(R.string.upload_settings_error)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message, onRetry = { viewModel.uploadSettings() })
    }

    private fun showUploadSettingsInternetError() {
        val title = getString(R.string.upload_settings_error)
        val message = getString(R.string.internet_connection_error_message)

        showErrorDialog(title, message, onRetry = { viewModel.uploadSettings() })
    }

    private fun showErrorDialog(title: String? = null, message: String? = null, onRetry: (() -> Unit)? = null) {
        supportFragmentManager.findFragmentByTag(ErrorDialog.TAG) as? ErrorDialog
            ?: ErrorDialog.newInstance(title, message)
                .also {
                    it.onRetryClick = onRetry
                    it.showNow(supportFragmentManager, ErrorDialog.TAG)
                }
    }

    private fun logOut() {
        viewModel.deleteUserData()
        AuthActivity.launch(this)
        finish()
    }

    private fun handleUserDataCorruption() {
        toast(R.string.user_data_corrupted_error, Toast.LENGTH_LONG)
        logOut()
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

}