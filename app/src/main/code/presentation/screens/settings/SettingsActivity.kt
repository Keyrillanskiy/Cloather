package presentation.screens.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R

/**
 * Экран настроек
 *
 * @author Keyrillanskiy
 * @since 22.02.2019, 10:10.
 */
class SettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

}