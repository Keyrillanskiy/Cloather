package data.repositories.interfaces

import domain.models.responses.WeatherResponse
import io.reactivex.Single

/**
 * Интерфейс репозитория для работы с погодой.
 *
 * @author Keyrillanskiy
 * @since 10.02.2019, 14:02.
 */
interface WeatherRepository {

    /**
     * Функция для получения текущей погоды и прогноза погоды.
     *
     * @param latitude Широта.
     * @param longitude Долгота.
     *
     * @return Класс с текущей погодой и прогнозом погоды.
     */
    fun fetchWeather(latitude: Double, longitude: Double): Single<WeatherResponse>

}