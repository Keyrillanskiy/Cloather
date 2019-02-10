package data.mappers.interfaces

import domain.models.entities.LocationRequest
import domain.models.values.Language

/**
 * @author Keyrillanskiy
 * @since 09.02.2019, 14:17.
 */
interface LocationRequestDataMapper {

    fun toLocationRequestData(
        language: Language,
        cellId: Int?,
        lac: Int?,
        simOperator: String?
    ): LocationRequest

}