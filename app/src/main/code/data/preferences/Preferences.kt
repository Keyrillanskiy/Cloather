package data.preferences

import android.content.Context
import domain.models.entities.User
import domain.models.values.Gender
import domain.models.values.PreferredWeather
import domain.models.values.toGender
import domain.models.values.toPreferredWeather

/**
 * Класс для работы с shared preferences
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 9:15.
 */
class Preferences(private val context: Context) {

    private val prefs by lazy { context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE) }

    fun fetchUser(): User {
        return User(
            id = uid,
            name = name,
            gender = gender,
            preferredWeather = preferredWeather,
            token = token,
            googleAvatarURL = googleAvatarUrl
        )
    }

    fun cacheUser(user: User) {
        uid = user.id
        name = user.name
        gender = user.gender
        preferredWeather = user.preferredWeather
        token = user.token
        googleAvatarUrl = user.googleAvatarURL
    }

    fun isUserAuthorized(): Boolean = uid != null

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(PREF_IS_FIRST_LAUNCH, true)
        set(value) {
            prefs.edit()
                .putBoolean(PREF_IS_FIRST_LAUNCH, value)
                .apply()
        }

    private var uid: String?
        get() = prefs.getString(PREF_UID, null)
        set(value) {
            prefs.edit()
                .putString(PREF_UID, value)
                .apply()
        }

    private var name: String?
        get() = prefs.getString(PREF_NAME, null)
        set(value) {
            prefs.edit()
                .putString(PREF_NAME, value)
                .apply()
        }

    private var gender: Gender
        get() {
            val string = prefs.getString(PREF_GENDER, "u")
            return string.toGender()
        }
        set(value) {
            prefs.edit()
                .putString(PREF_GENDER, value.toString())
                .apply()
        }

    private var preferredWeather: PreferredWeather
        get() {
            val string = prefs.getString(PREF_PREFERRED_WEATHER, "n")
            return string.toPreferredWeather()
        }
        set(value) {
            prefs.edit()
                .putString(PREF_PREFERRED_WEATHER, value.toString())
                .apply()
        }

    private var token: String?
        get() = prefs.getString(PREF_TOKEN, null)
        set(value) {
            prefs.edit()
                .putString(PREF_TOKEN, value)
                .apply()
        }

    private var googleAvatarUrl: String?
        get() = prefs.getString(PREF_GOOGLE_AVATAR_URL, null)
        set(value) {
            prefs.edit()
                .putString(PREF_GOOGLE_AVATAR_URL, value)
                .apply()
        }

    private companion object {
        private const val PREFERENCES_NAME = "CloatherPrefs"

        private const val PREF_IS_FIRST_LAUNCH = "isFirstLaunch"
        private const val PREF_UID = "uid"
        private const val PREF_NAME = "name"
        private const val PREF_GENDER = "gender"
        private const val PREF_PREFERRED_WEATHER = "prefWeather"
        private const val PREF_TOKEN = "token"
        private const val PREF_GOOGLE_AVATAR_URL = "googleAvatarURL"
    }

}