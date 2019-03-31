package data.network.apiSpecs

import domain.models.responses.WeatherResponse
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

    /**
     * Запрос на сервер для получения текущей погоды и прогноза погоды.
     *
     * @param latitude Широта.
     * @param longitude Долгота.
     *
     * @return Класс с текущей погодой и прогнозом погоды.
     */
    @GET("/api/weather/yandex")
    fun fetchWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("lang") language: String): Single<WeatherResponse>

}