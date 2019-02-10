package domain.models.values

/**
 * Виды локализации в приложении для получения геолокации
 *
 * @author Keyrillanskiy
 * @since 08.02.2019, 17:39.
 */
enum class Language(val value: String) {
    RUSSIAN("ru"), ENGLISH("en")
}

fun defineLanguage(language: String): Language {
    return Language.values().find { it.value == language } ?: Language.ENGLISH
}