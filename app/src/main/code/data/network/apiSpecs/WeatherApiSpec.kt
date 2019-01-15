package data.network.apiSpecs

import domain.models.responses.Weather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Интерфейс для запросов, связанных с погодой
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 21:35.
 */
interface WeatherApiSpec {

    @GET("/api/weather/yandex")
    fun fetchWeather(@Query("lat") lat: Double?, @Query("lon") lon: Double?): Single<Weather>

}