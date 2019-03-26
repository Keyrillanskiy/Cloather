package domain.models.values

import androidx.annotation.DrawableRes
import com.github.keyrillanskiy.cloather.R

/**
 * Перечисление всех типов погоды и соответствующие им названия на русском и английском,
 * а также нужная иконка погоды.
 *
 * @author Keyrillanskiy
 * @since 25.03.2019, 20:35.
 */
enum class WeatherType(val inEnglish: String, val inRussian: String, @DrawableRes val drawableRes: Int) {
    CLEAR("clear", "ясно", R.drawable.ic_weather_sunny),
    MOSTLY_CLEAR("mostly clear", "малооблачно", R.drawable.ic_weather_cloudy),
    PARTLY_CLOUDY("partly cloudy", "малооблачно", R.drawable.ic_weather_cloudy),
    CLOUDY("cloudy", "облачно", R.drawable.ic_weather_cloudy),
    OVERCAST("overcast", "пасмурно", R.drawable.ic_weather_overcast),
    LIGHT_RAIN("light rain", "небольшой дождь", R.drawable.ic_weather_rain),
    RAIN("rain", "дождь", R.drawable.ic_weather_rain),
    HARD_RAIN("hard rain", "сильный дождь", R.drawable.ic_weather_hard_rain),
    THUNDERSTORMS("thunderstorms with rain", "сильный дождь, гроза", R.drawable.ic_weather_lightning),
    WET_SNOW("wet snow", "Дождь со снегом", R.drawable.ic_weather_rain),
    LIGHT_SNOW("light snow", "небольшой снег", R.drawable.ic_weather_snow),
    SNOW("snow", "снег", R.drawable.ic_weather_snow),
    SNOWFALL("snowfall", "снегопад", R.drawable.ic_weather_snow)
}