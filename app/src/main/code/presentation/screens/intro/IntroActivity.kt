package presentation.screens.intro

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.keyrillanskiy.cloather.R
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import presentation.screens.main.MainActivity

/**
 * Экран туториала, который должен запускаться только один раз после установки приложения
 *
 * @author Keyrillanskiy
 * @since 14.01.2019, 23:27.
 */
class IntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntro2Fragment.newInstance(
                "title", "description", R.mipmap.ic_launcher, ContextCompat.getColor(this, R.color.accent)
            )
        )
        addSlide(
            AppIntro2Fragment.newInstance(
                "title2", "description2", R.mipmap.ic_launcher, ContextCompat.getColor(this, R.color.primary)
            )
        )
        addSlide(
            AppIntro2Fragment.newInstance(
                "title3", "description3", R.mipmap.ic_launcher, ContextCompat.getColor(this, R.color.primaryDark)
            )
        )

        //configuration
        showSkipButton(true)
        setVibrate(false)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        openMainScreen()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        openMainScreen()
    }

    private fun openMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}