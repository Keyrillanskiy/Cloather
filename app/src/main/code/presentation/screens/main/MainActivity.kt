package presentation.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R

/**
 * Главный экран приложения
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 16:28.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}