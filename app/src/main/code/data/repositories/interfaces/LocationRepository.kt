package data.repositories.interfaces

import domain.models.entities.LocationRequest
import domain.models.responses.LocationResponse
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 08.02.2019, 17:38.
 */
interface LocationRepository {
    fun fetchLocation(locationRequestData: LocationRequest): Single<LocationResponse>
}