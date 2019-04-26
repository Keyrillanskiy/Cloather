package data.mappers.implementations

import android.content.Context
import androidx.annotation.DrawableRes
import com.github.keyrillanskiy.cloather.R
import data.mappers.interfaces.WeatherMapper
import domain.models.responses.*
import domain.models.values.Language
import domain.models.values.WeatherType
import presentation.screens.weather.WeatherCurrentItemData
import presentation.screens.weather.WeatherForecastItemData
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Keyrillanskiy
 * @since 25.03.2019, 18:22.
 */
class WeatherMapperImpl(private val context: Context) : WeatherMapper {

    override fun toCurrentWeatherItemData(
        weatherResponse: WeatherResponse,
        language: Language
    ): WeatherCurrentItemData {
        val currentWeather = weatherResponse.currentWeather
        return WeatherCurrentItemData(
            city = weatherResponse.city,
            temperature = currentWeather.temperature(),
            feelsLike = currentWeather.feelsLike(context),
            windProperties = currentWeather.windProperties(context),
            pressure = currentWeather.pressure(context),
            humidity = currentWeather.humidity(context),
            weatherBackgroundRes = currentWeather.type.toWeatherBackgroundRes(),
            type = currentWeather.type.toWeatherType(),
            currentLanguage = language
        )
    }

    override fun toForecastWeatherItemData(weatherForecast: Forecast): WeatherForecastItemData {
        val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = dateFormatter.format(Date(weatherForecast.time * 1000))
        return WeatherForecastItemData(
            time = time,
            type = weatherForecast.type.toWeatherType(),
            temperature = weatherForecast.temperature()
        )
    }

    private fun String.toWeatherType(): WeatherType {
        return when (this) {
            "clear" -> WeatherType.CLEAR
            "mostly-clear" -> WeatherType.MOSTLY_CLEAR
            "partly-cloudy" -> WeatherType.PARTLY_CLOUDY
            "cloudy" -> WeatherType.CLOUDY
            "overcast" -> WeatherType.OVERCAST
            "partly-cloudy-and-light-rain", "cloudy-and-light-rain", "cloudy-and-rain", "overcast-and-light-rain" ->
                WeatherType.LIGHT_RAIN
            "partly-cloudy-and-rain" -> WeatherType.RAIN
            "overcast-and-rain" -> WeatherType.HARD_RAIN
            "overcast-thunderstorms-with-rain" -> WeatherType.THUNDERSTORMS
            "overcast-and-wet-snow" -> WeatherType.WET_SNOW
            "partly-cloudy-and-light-snow", "overcast-and-light-snow", "cloudy-and-light-snow" -> WeatherType.LIGHT_SNOW
            "partly-cloudy-and-snow", "cloudy-and-snow" -> WeatherType.SNOW
            "overcast-and-snow" -> WeatherType.SNOWFALL
            else -> throw IllegalArgumentException("unknown weather type: $this")
        }
    }

    @DrawableRes
    private fun String.toWeatherBackgroundRes(): Int? {
        return when (this) {
            "clear", "mostly-clear" -> R.drawable.ic_weather_bg_sunny
            "cloudy", "partly-cloudy" -> R.drawable.ic_weather_bg_cloudy
            "overcast" -> R.drawable.ic_weather_bg_overcast
            "partly-cloudy-and-light-rain", "partly-cloudy-and-rain", "cloudy-and-light-rain", "cloudy-and-rain",
            "overcast-and-light-rain", "overcast-and-rain" -> R.drawable.ic_weather_bg_rain
            "overcast-thunderstorms-with-rain" -> R.drawable.ic_weather_bg_thunderstorm_rain
            "overcast-and-wet-snow", "partly-cloudy-and-light-snow", "overcast-and-light-snow", "cloudy-and-light-snow",
            "partly-cloudy-and-snow", "cloudy-and-snow", "overcast-and-snow" -> R.drawable.ic_weather_bg_overcast_snow
            else -> null
        }
    }

}