package data.mappers.interfaces

import domain.models.responses.Forecast
import domain.models.responses.WeatherResponse
import domain.models.values.Language
import presentation.screens.weather.WeatherCurrentItemData
import presentation.screens.weather.WeatherForecastItemData

/**
 * @author Keyrillanskiy
 * @since 25.03.2019, 18:20.
 */
interface WeatherMapper {

    fun toCurrentWeatherItemData(weatherResponse: WeatherResponse, language: Language): WeatherCurrentItemData

    fun toForecastWeatherItemData(weatherForecast: Forecast): WeatherForecastItemData

}