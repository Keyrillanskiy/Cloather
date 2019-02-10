package data.useCases.implementations

import data.mappers.interfaces.LocationRequestDataMapper
import data.repositories.interfaces.LocationRepository
import data.useCases.interfaces.LocationUseCase
import domain.models.entities.LocationRequest
import domain.models.responses.LocationResponse
import domain.models.values.Language
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 08.02.2019, 21:35.
 */
class LocationUseCaseImpl(
    private val locationRepository: LocationRepository,
    private val mapper: LocationRequestDataMapper
) : LocationUseCase {

    override fun fetchLocation(
        language: Language,
        cellId: Int?,
        lac: Int?,
        simOperator: String?
    ): Single<LocationResponse> {
        val locationRequestData = mapper.toLocationRequestData(
            language = language,
            cellId = cellId,
            lac = lac,
            simOperator = simOperator
        )

        return locationRepository.fetchLocation(locationRequestData)
    }

    override fun fetchLocation(
        language: Language,
        cellId: Int?,
        lac: Int?,
        countryCode: Int?,
        operatorCode: Int?
    ): Single<LocationResponse> {
        val locationRequestData = LocationRequest(
            language = language,
            cellId = cellId,
            lac = lac,
            countryCode = countryCode,
            operatorId = operatorCode
        )

        return locationRepository.fetchLocation(locationRequestData)
    }

}