package presentation.screens.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R
import com.tinder.StateMachine
import extensions.toast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.screens.auth.AuthActivity
import presentation.screens.gender.GenderActivity
import presentation.screens.intro.IntroActivity
import utils.PermissionUtils

/**
 * Главный экран приложения
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 16:28.
 */

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var viewHolder: MainViewHolder
    private val stateMachine = StateMachine.create<State, Event, SideEffect> {
        initialState(State.WithoutPermission)

        state<State.WithoutPermission> {
            on<Event.RequestPermission> {
                transitionTo(State.PermissionRequested, SideEffect.PermissionRequested)
            }
            on<Event.PermissionAlreadyGranted> {
                transitionTo(State.WithPermission, SideEffect.PermissionGranted)
            }
        }

        state<State.PermissionRequested> {
            on<Event.PermissionGranted> {
                transitionTo(State.WithPermission, SideEffect.PermissionGranted)
            }
            on<Event.PermissionRejected> {
                transitionTo(State.WithoutPermission, SideEffect.PermissionRejected)
            }
        }

        state<State.WithPermission> {
            //TODO
        }

        onTransition { transition ->
            val validTransition = transition as? StateMachine.Transition.Valid ?: return@onTransition

            when (validTransition.sideEffect) {
                SideEffect.PermissionRequested -> onEventPermissionRequested()
                SideEffect.PermissionGranted -> onEventPermissionGranted()
                SideEffect.PermissionRejected -> onEventPermissionRejected()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar) // потому что по умолчанию стоит тема для splash
        super.onCreate(savedInstanceState)

        // routing
        if (viewModel.isFirstLaunch()) {
            IntroActivity.launch(this)
            finish()
        } else if (!viewModel.isUserAuthorized()) {
            AuthActivity.launch(this)
            finish()
        } else if (viewModel.isGenderUndefined()) {
            GenderActivity.launch(this)
            finish()
        } else {
            initMainScreen()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if ((requestCode == RC_LOCATION_PERMISSION
                    && grantResults.isNotEmpty()
                    && grantResults.all { it == PackageManager.PERMISSION_GRANTED })
        ) {
            stateMachine.transition(Event.PermissionGranted)
        } else {
            stateMachine.transition(Event.PermissionRejected)
        }
    }

    private fun initMainScreen() {
        setContentView(R.layout.activity_main)

        viewHolder = MainViewHolder(mainContent).setup {
            onWardrobeClick = { toast("Not implemented") }
            onSettingsClick = { toast("Not implemented") }
        }

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        // starting the state machine
        if (PermissionUtils.isLocationPermissionGranted(this)) {
            stateMachine.transition(Event.PermissionAlreadyGranted)
        } else {
            stateMachine.transition(Event.RequestPermission)
        }
    }

    private fun showLocationPermissionReasonDialog() {
        (supportFragmentManager.findFragmentByTag(RequestLocationDialog.TAG) as? RequestLocationDialog
            ?: RequestLocationDialog())
            .also {
                it.onPermitClick = { PermissionUtils.requestLocationPermission(this, RC_LOCATION_PERMISSION) }
            }
            .showNow(supportFragmentManager, RequestLocationDialog.TAG)
    }

    private fun onEventPermissionRequested() {
        showLocationPermissionReasonDialog()
    }

    private fun onEventPermissionGranted() {
        // fetch weather
        toast("GRANTED")
    }

    private fun onEventPermissionRejected() {
        toast("REJECTED")
    }

    sealed class State {
        object WithoutPermission : State()
        object PermissionRequested : State()
        object WithPermission : State()
    }

    sealed class Event {
        object RequestPermission : Event()
        object PermissionGranted : Event()
        object PermissionAlreadyGranted : Event()
        object PermissionRejected : Event()
    }

    sealed class SideEffect {
        object PermissionRequested : SideEffect()
        object PermissionGranted : SideEffect()
        object PermissionRejected : SideEffect()
    }

    companion object {
        private const val RC_LOCATION_PERMISSION = 1

        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}