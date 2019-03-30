package presentation.screens.main

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.github.keyrillanskiy.cloather.R
import kotlinx.android.synthetic.main.activity_main.view.*
import presentation.common.BaseViewHolder
import presentation.screens.main.MainPagerAdapter.Companion.SETTINGS_FRAGMENT_POSITION
import presentation.screens.main.MainPagerAdapter.Companion.WARDROBE_FRAGMENT_POSITION
import presentation.screens.main.MainPagerAdapter.Companion.WEATHER_FRAGMENT_POSITION
import presentation.screens.wardrobe.WardrobeFragment

/**
 * @author Keyrillanskiy
 * @since 13.03.2019, 8:51.
 */
class MainViewHolder(
    private val rootView: View,
    private val fragmentManager: FragmentManager
) : BaseViewHolder(rootView) {

    fun setup(init: MainViewHolder.() -> Unit): MainViewHolder {
        init.invoke(this)

        with(rootView) {
            val adapter = MainPagerAdapter(fragmentManager)

            mainViewPager.offscreenPageLimit = 3
            mainViewPager.adapter = adapter

            mainBottomNavigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.weather -> {
                        mainViewPager.currentItem = WEATHER_FRAGMENT_POSITION
                    }
                    R.id.wardrobe -> {
                        mainViewPager.currentItem = WARDROBE_FRAGMENT_POSITION
                    }
                    R.id.settings -> {
                        mainViewPager.currentItem = SETTINGS_FRAGMENT_POSITION
                    }
                }
                true
            }
        }

        return this
    }

}