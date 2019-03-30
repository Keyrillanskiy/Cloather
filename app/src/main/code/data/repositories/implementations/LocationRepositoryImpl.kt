package data.repositories.implementations

import data.network.apiSpecs.LocationApiSpec
import data.repositories.interfaces.LocationRepository
import domain.models.entities.LocationRequest
import domain.models.responses.LocationResponse
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 08.02.2019, 17:50.
 */
class LocationRepositoryImpl(private val locationApi: LocationApiSpec) : LocationRepository {

    override fun fetchLocation(locationRequestData: LocationRequest): Single<LocationResponse> {
        return locationApi.fetchGeolocation(locationRequestData)
    }

}