package presentation.screens.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import presentation.screens.settings.SettingsFragment
import presentation.screens.wardrobe.WardrobeFragment
import presentation.screens.weather.WeatherFragment

/**
 * @author Keyrillanskiy
 * @since 13.03.2019, 8:39.
 */
class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            WEATHER_FRAGMENT_POSITION -> WeatherFragment()
            WARDROBE_FRAGMENT_POSITION -> WardrobeFragment()
            SETTINGS_FRAGMENT_POSITION -> SettingsFragment()
            else -> throw IllegalArgumentException("illegal item position in view pager: $position")
        }
    }

    override fun getCount() = PAGES_COUNT

    companion object {
        private const val PAGES_COUNT = 3
        const val WEATHER_FRAGMENT_POSITION = 0
        const val WARDROBE_FRAGMENT_POSITION = 1
        const val SETTINGS_FRAGMENT_POSITION = 2
    }

}