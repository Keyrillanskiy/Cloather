package data.network.apiSpecs

import domain.models.entities.LocationRequest
import domain.models.responses.LocationResponse
import io.reactivex.Single

/**
 * Интерфейс для запросов, связанных с геолокацией.
 *
 * @author Keyrillanskiy
 * @since 08.02.2019, 17:51.
 */
interface LocationApiSpec {

    /**
     * Получение геолокации
     */
    fun fetchGeolocation(locationRequestData: LocationRequest): Single<LocationResponse>

}