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
import presentation.screens.settings.SettingsInteractor
import presentation.screens.wardrobe.WardrobeInteractor
import presentation.screens.weather.RequestLocationDialog
import presentation.screens.weather.WeatherInteractor
import presentation.share.ErrorDialog
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig
import utils.NetUtils


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
        setTheme(com.github.keyrillanskiy.cloather.R.style.AppTheme_NoActionBar) // потому что по умолчанию стоит тема для splash
        super.onCreate(savedInstanceState)

        // routing
        when {
            !viewModel.isUserAuthorized() -> {
                AuthActivity.launch(this)
                finish()
            }
            viewModel.isGenderUndefined() -> {
                GenderActivity.launch(this)
                finish()
            }
            else -> initMainScreen()
        }
    }

    override fun onShowLoading() {
        progressIndicator.visible()
    }

    override fun onHideLoading() {
        progressIndicator.gone()
    }

    override fun onInternetError() {
        val title = getString(com.github.keyrillanskiy.cloather.R.string.internet_error_title)
        val message = getString(com.github.keyrillanskiy.cloather.R.string.internet_connection_error_message)

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
                    dialog.show(supportFragmentManager, ErrorDialog.TAG)
                }
    }

    override fun onShowCase() {
        if (!viewModel.preferences.isFirstLaunch) {
            return
        }

        val config = ShowcaseConfig().apply {
            delay = 500
        }
        val circleItemOneString = getString(R.string.tutorial_circle_one)
        val circleItemTwoString = getString(R.string.tutorial_circle_two)
        val circleItemThreeString = getString(R.string.tutorial_circle_three)
        val gotItString = getString(R.string.got_it)
        val sequence = MaterialShowcaseSequence(this, SHOWCASE_ID).apply {
            setConfig(config)
            addSequenceItem(mainBottomNavItem1, circleItemOneString, gotItString)
            addSequenceItem(mainBottomNavItem2, circleItemTwoString, gotItString)
            addSequenceItem(mainBottomNavItem3, circleItemThreeString, gotItString)
        }
        sequence.start()

        viewModel.preferences.isFirstLaunch = false
    }

    private fun initMainScreen() {
        setContentView(com.github.keyrillanskiy.cloather.R.layout.activity_main)
        viewHolder = MainViewHolder(root, supportFragmentManager).setup { /*empty*/ }
    }

    override fun onSettingsChanged() {
        NetUtils.withNetConnection(
            onSuccess = {
                viewModel.uploadSettings()
            }, onError = {
                showUploadSettingsInternetError()
            })
    }

    private fun showUploadSettingsInternetError() {
        val title = getString(com.github.keyrillanskiy.cloather.R.string.upload_settings_error)
        val message = getString(com.github.keyrillanskiy.cloather.R.string.internet_connection_error_message)

        onShowErrorDialog(title, message, onRetryClick = { viewModel.uploadSettings() })
    }

    companion object {
        private const val SHOWCASE_ID = "com.github.keyrillanskiy.main.showcase"

        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}