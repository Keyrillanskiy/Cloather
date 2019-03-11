package presentation.screens.wardrobe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.keyrillanskiy.cloather.R
import extensions.gone
import extensions.visible
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_wardrobe.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.common.Failure
import presentation.common.Loading
import presentation.common.Success
import presentation.share.ErrorDialog

/**
 * Экран выбора гардероба.
 *
 * @author Keyrillanskiy
 * @since 27.02.2019, 12:21.
 */
class WardrobeActivity : AppCompatActivity(), WardrobeInteractor {

    private val viewModel by viewModel<WardrobeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)
        viewModel.observe()
    }

    private fun WardrobeViewModel.observe() {
        categoriesLiveData.observe(this@WardrobeActivity, Observer { response ->
            when (response) {
                is Loading, is Success -> {
                    /*nothing*/
                }
                is Failure -> showUnknownError()
            }
        })

        thingsLiveData.observe(this@WardrobeActivity, Observer { response ->
            when (response) {
                is Loading, is Success -> {
                    /*nothing*/
                }
                is Failure -> showUnknownError()
            }
        })

        thingUpdateLiveData.observe(this@WardrobeActivity, Observer { response ->
            when (response) {
                is Loading, is Success -> {
                    /*nothing*/
                }
                is Failure -> showUnknownError()
            }
        })
    }

    override fun onShowLoading() {
        progressIndicator.visible()
    }

    override fun onHideLoading() {
        progressIndicator.gone()
    }

    override fun onInternetError() {
        showInternetError()
    }

    private fun showInternetError() {
        val title = getString(R.string.internet_error_title)
        val message = getString(R.string.internet_connection_error_message)

        showErrorDialog(title, message)
    }

    private fun showUnknownError() {
        val title = getString(R.string.internet_error_title)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message)
    }

    private fun showErrorDialog(title: String? = null, message: String? = null, onRetry: (() -> Unit)? = null) {
        supportFragmentManager.findFragmentByTag(ErrorDialog.TAG) as? ErrorDialog
            ?: ErrorDialog.newInstance(title, message)
                .also {
                    it.onRetryClick = onRetry
                    it.showNow(supportFragmentManager, ErrorDialog.TAG)
                }
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, WardrobeActivity::class.java))
        }
    }

}