package presentation.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.keyrillanskiy.cloather.R
import extensions.toast
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Главный экран приложения
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 16:28.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var viewHolder: MainViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewHolder = MainViewHolder(mainContent).setup {
            onWardrobeClick = { toast("Not implemented") }
            onSettingsClick = { toast("Not implemented") }
        }
    }

}