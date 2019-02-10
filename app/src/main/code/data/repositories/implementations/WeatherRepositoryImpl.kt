package data.repositories.implementations

import data.network.CloatherHttpClient
import data.repositories.interfaces.WeatherRepository
import domain.models.responses.WeatherResponse
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 10.02.2019, 14:07.
 */
class WeatherRepositoryImpl(httpClient: CloatherHttpClient): WeatherRepository {

    private val weatherApi = httpClient.weatherApi

    override fun fetchWeather(latitude: Double, longitude: Double): Single<WeatherResponse> {
        return weatherApi.fetchWeather(latitude, longitude)
    }

}