package presentation.screens.main

import androidx.lifecycle.ViewModel
import data.preferences.Preferences


/**
 * @author Keyrillanskiy
 * @since 19.01.2019, 15:29.
 */
class MainViewModel(private val preferences: Preferences) : ViewModel() {

    fun isFirstLaunch() = preferences.isFirstLaunch

    fun isUserAuthorized() = preferences.isUserAuthorized()

}