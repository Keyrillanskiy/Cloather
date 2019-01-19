package presentation.screens.gender

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.keyrillanskiy.cloather.R
import com.jakewharton.rxbinding3.view.clicks
import domain.models.values.Gender
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_gender.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.common.Failure
import presentation.common.Loading
import presentation.common.Success
import presentation.screens.auth.AuthActivity
import presentation.screens.main.MainActivity
import presentation.share.ErrorDialog
import presentation.share.ProgressDialog
import utils.NetUtils
import java.util.concurrent.TimeUnit

/**
 * Экран выбора пола
 *
 * @author Keyrillanskiy
 * @since 19.01.2019, 16:55.
 */
class GenderActivity : AppCompatActivity() {

    private val viewModel by viewModel<GenderViewModel>()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.isGenderUndefined()) {
            if (viewModel.isUserAuthorized()) {
                initGenderScreen()
            } else {
                AuthActivity.launch(this)
                finish()
            }
        } else {
            openMainScreen()
        }
    }

    private fun initGenderScreen() {
        setContentView(R.layout.activity_gender)

        val clicksSubject = PublishSubject.create<GenderClick>()
        clicksSubject.throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { click ->
                when (click) {
                    GenderClick.MAN_CLICK -> sendGender(Gender.MALE)
                    GenderClick.WOMAN_CLICK -> sendGender(Gender.FEMALE)
                }
            }
            .addTo(disposables)

        genderManButton.clicks()
            .subscribe { clicksSubject.onNext(GenderClick.MAN_CLICK) }
            .addTo(disposables)

        genderWomanButton.clicks()
            .subscribe { clicksSubject.onNext(GenderClick.WOMAN_CLICK) }
            .addTo(disposables)

        viewModel.observeData()
    }

    private fun sendGender(gender: Gender) {
        NetUtils.withNetConnection(
            onSuccess = { viewModel.sendGender(gender) },
            onError = { showInternetGenderError() }
        )
    }

    private fun showGenderProgress() {
        supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog
            ?: ProgressDialog.newInstance(getString(R.string.saving_your_gender))
                .also { it.showNow(supportFragmentManager, ProgressDialog.TAG) }
    }

    private fun hideGenderProgress() {
        (supportFragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog)?.dismiss()
    }

    private fun showInternetGenderError() {
        val title = getString(R.string.saving_your_gender_error_title)
        val message = getString(R.string.internet_connection_error_message)

        showErrorDialog(title, message)
    }

    private fun showUnknownGenderError() {
        val title = getString(R.string.saving_your_gender_error_title)
        val message = getString(R.string.unknown_error)

        showErrorDialog(title, message)
    }

    private fun showErrorDialog(title: String? = null, message: String? = null) {
        supportFragmentManager.findFragmentByTag(ErrorDialog.TAG) as? ErrorDialog
            ?: ErrorDialog.newInstance(title, message)
                .also {
                    it.onOkClick = { hideGenderProgress() }
                    it.showNow(supportFragmentManager, ErrorDialog.TAG)
                }
    }

    private fun openMainScreen() {
        MainActivity.launch(this)
        finish()
    }

    private fun GenderViewModel.observeData() {
        genderLiveData.observe(this@GenderActivity, Observer { response ->
            when (response) {
                is Loading -> showGenderProgress()
                is Success -> {
                    viewModel.saveGender(response.value)
                    hideGenderProgress()
                    openMainScreen()
                }
                is Failure -> {
                    hideGenderProgress()
                    showUnknownGenderError()
                }
            }
        })
    }

    enum class GenderClick {
        MAN_CLICK, WOMAN_CLICK
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, GenderActivity::class.java))
        }
    }

}