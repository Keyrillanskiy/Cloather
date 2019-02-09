package data.mappers.implementations

import data.mappers.interfaces.LocationRequestDataMapper
import domain.models.entities.LocationRequest
import domain.models.values.Language

/**
 * @author Keyrillanskiy
 * @since 09.02.2019, 14:18.
 */
class LocationRequestDataMapperImpl : LocationRequestDataMapper {

    override fun toLocationRequestData(
        language: Language,
        cellId: Int?,
        lac: Int?,
        simOperator: String?
    ): LocationRequest {
        return LocationRequest(
            language = language,
            cellId = cellId,
            lac = lac,
            countryCode = simOperator?.take(3)?.toInt(),
            operatorId = simOperator?.substring(3)?.toInt()
        )
    }

}