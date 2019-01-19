package presentation.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R
import extensions.toast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.screens.auth.AuthActivity
import presentation.screens.intro.IntroActivity

/**
 * Главный экран приложения
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 16:28.
 */

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var viewHolder: MainViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar) // потому что по умолчанию стоит тема для splash
        super.onCreate(savedInstanceState)

        if (viewModel.isFirstLaunch()) {
            IntroActivity.launch(this)
            finish()
        } else if (!viewModel.isUserAuthorized()) {
            AuthActivity.launch(this)
            finish()
        } else {
            initMainScreen()
        }
    }

    private fun initMainScreen() {
        setContentView(R.layout.activity_main)

        viewHolder = MainViewHolder(mainContent).setup {
            onWardrobeClick = { toast("Not implemented") }
            onSettingsClick = { toast("Not implemented") }
        }
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}