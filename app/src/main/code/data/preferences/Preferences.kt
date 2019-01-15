package data.preferences

import android.content.Context

/**
 * Класс для работы с shared preferences
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 9:15.
 */
class Preferences(private val context: Context) {

    private val prefs by lazy { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    private companion object {
        private const val PREFS_NAME = "CloatherPrefs"
    }

}