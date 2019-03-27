package data.mappers.implementations

import data.mappers.interfaces.WeatherMapper
import domain.models.responses.Forecast
import domain.models.responses.WeatherResponse
import domain.models.responses.temperature
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
class WeatherMapperImpl : WeatherMapper {

    override fun toCurrentWeatherItemData(
        weatherResponse: WeatherResponse,
        language: Language
    ): WeatherCurrentItemData {
        val currentWeather = weatherResponse.currentWeather
        return WeatherCurrentItemData(
            city = weatherResponse.city,
            temperature = currentWeather.temperature(),
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

}