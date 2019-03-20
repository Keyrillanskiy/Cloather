package presentation.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import extensions.gone
import extensions.visible
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.screens.auth.AuthActivity
import presentation.screens.gender.GenderActivity
import presentation.screens.intro.IntroActivity
import presentation.screens.settings.SettingsInteractor
import presentation.screens.wardrobe.WardrobeInteractor
import presentation.screens.weather.RequestLocationDialog
import presentation.screens.weather.WeatherInteractor
import presentation.share.ErrorDialog
import utils.NetUtils
import utils.googleAuthClientId

/**
 * Активити, которая содержит viewPager с экранами погоды, гардероба и настроек.
 *
 * @author Keyrillanskiy
 * @since 13.03.2019, 8:19.
 */
class MainActivity : AppCompatActivity(), WeatherInteractor, WardrobeInteractor, SettingsInteractor {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var viewHolder: MainViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar) // потому что по умолчанию стоит тема для splash
        super.onCreate(savedInstanceState)

        // routing
        when {
            viewModel.isFirstLaunch() -> {
                IntroActivity.launch(this)
                finish()
            }
            !viewModel.isUserAuthorized() -> {
                AuthActivity.launch(this)
                finish()
            }
            viewModel.isGenderUndefined() -> {
                GenderActivity.launch(this)
                finish()
            }
            else -> {
                initMainScreen()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onShowLoading() {
        progressIndicator.visible()
    }

    override fun onHideLoading() {
        progressIndicator.gone()
    }

    override fun onInternetError() {
        val title = getString(R.string.internet_error_title)
        val message = getString(R.string.internet_connection_error_message)

        onShowErrorDialog(title, message)
    }

    override fun onLogOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        GoogleSignIn.getClient(this, gso).signOut()

        viewModel.deleteUserData()
        AuthActivity.launch(this)
        finish()
    }

    override fun onShowLocationPermissionReasonDialog(
        onPermitClick: (() -> Unit)?,
        onCancelClick: (() -> Unit)?
    ) {
        supportFragmentManager.findFragmentByTag(RequestLocationDialog.TAG) as? RequestLocationDialog
            ?: RequestLocationDialog()
                .also {
                    it.onPermitClick = { onPermitClick?.invoke() }
                    it.onCancelClick = { onCancelClick?.invoke() }
                    it.show(supportFragmentManager, RequestLocationDialog.TAG)
                }
    }

    override fun onShowErrorDialog(
        title: String?,
        message: String?,
        onOkClick: (() -> Unit)?,
        onRetryClick: (() -> Unit)?
    ) {
        supportFragmentManager.findFragmentByTag(ErrorDialog.TAG) as? ErrorDialog
            ?: ErrorDialog.newInstance(title, message)
                .also { dialog ->
                    onOkClick?.let { dialog.onOkClick = { it.invoke() } }
                    onRetryClick?.let { dialog.onRetryClick = { it.invoke() } }
                    dialog.showNow(supportFragmentManager, ErrorDialog.TAG)
                }
    }

    private fun initMainScreen() {
        setContentView(R.layout.activity_main)
        viewHolder = MainViewHolder(root, supportFragmentManager).setup {
            onSettingsLeaved = { uploadSettings() }
        }
    }

    private fun uploadSettings() {
        NetUtils.withNetConnection(
            onSuccess = {
                viewModel.uploadSettings()
            }, onError = {
                showUploadSettingsInternetError()
            })
    }

    private fun showUploadSettingsInternetError() {
        val title = getString(R.string.upload_settings_error)
        val message = getString(R.string.internet_connection_error_message)

        onShowErrorDialog(title, message, onRetryClick = { viewModel.uploadSettings() })
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}