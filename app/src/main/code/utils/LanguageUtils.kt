package utils

import android.content.res.Resources
import android.os.Build
import domain.models.values.Language
import domain.models.values.defineLanguage

/**
 * @author Keyrillanskiy
 * @since 30.03.2019, 21:26.
 */
object LanguageUtils {

    fun getSystemLanguage(resources: Resources): Language {
        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }

        return defineLanguage(currentLocale.language)
    }

}