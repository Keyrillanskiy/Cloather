package domain.models.values

/**
 * @author Keyrillanskiy
 * @since 18.01.2019, 20:39.
 */

enum class PreferredWeather(val value: Char) {
    HOT('h'), NEUTRAL('n'), COLD('c')
}

fun String.toPreferredWeather(): PreferredWeather {
    val prefWeathers = PreferredWeather.values()
    return prefWeathers.first { it.value == this.first() }
}