package data.repositories.interfaces

import domain.models.responses.Thing
import io.reactivex.Single

/**
 * Интерфейс репозитория для работы с одеждой.
 *
 * @author Keyrillanskiy
 * @since 10.02.2019, 17:15.
 */
interface WardrobeRepository {

    /**
     * Функция для получения набора одежды для текущей погоды.
     *
     * @param latitude Широта.
     * @param longitude Долгота.
     * @param token Токен пользователя.
     */
    fun fetchWhatToWear(latitude: Double, longitude: Double, token: String): Single<List<Thing>>

}