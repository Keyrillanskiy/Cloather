package domain.models.responses

import android.content.Context
import com.github.keyrillanskiy.cloather.R
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * Модели для работы с погодой
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 20:45.
 */

data class WeatherResponse(
    val city: String,
    @SerializedName("current") val currentWeather: CurrentWeather,
    val forecast: List<Forecast>
)

data class CurrentWeather(
    @SerializedName("temp") val temperature: Int,
    @SerializedName("feelsLikeTemp") val feelsLikeTemperature: Int,
    @SerializedName("windSpeed") val windSpeed: Int,
    @SerializedName("windDir") val windDirection: String,
    @SerializedName("pressurePa") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("type") val type: String
)

fun CurrentWeather.temperature(): String {
    return "${temperature}˚"
}

fun CurrentWeather.feelsLike(context: Context): String {
    return context.getString(R.string.feels_like, feelsLikeTemperature)
}

fun CurrentWeather.windProperties(context: Context): String {
    return context.getString(R.string.wind_properties, windSpeed)
}

fun CurrentWeather.pressure(context: Context): String {
    return context.getString(R.string.pressure, pressure)
}

fun CurrentWeather.humidity(context: Context): String {
    return context.getString(R.string.humidity, humidity)
}

data class Forecast(
    val time: Long,
    val type: String,
    @SerializedName("temp") val temperature: Int
)

fun Forecast.time(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(time * 1000))
}

fun Forecast.temperature(): String {
    return "$temperature˚"
}