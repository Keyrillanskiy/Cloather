package data.repositories.implementations

import android.content.res.Resources
import data.network.CloatherHttpClient
import data.repositories.interfaces.WeatherRepository
import domain.models.responses.WeatherResponse
import io.reactivex.Single
import utils.LanguageUtils

/**
 * @author Keyrillanskiy
 * @since 10.02.2019, 14:07.
 */
class WeatherRepositoryImpl(httpClient: CloatherHttpClient, private val resources: Resources): WeatherRepository {

    private val weatherApi = httpClient.weatherApi

    override fun fetchWeather(latitude: Double, longitude: Double): Single<WeatherResponse> {
        return weatherApi.fetchWeather(latitude, longitude, LanguageUtils.getSystemLanguage(resources).value)
    }

}