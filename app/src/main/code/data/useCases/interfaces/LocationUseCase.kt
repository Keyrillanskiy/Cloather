package data.useCases.interfaces

import domain.models.responses.LocationResponse
import domain.models.values.Language
import io.reactivex.Single

/**
 * Интерфейс UseCase для работы с геолокацией.
 *
 * @author Keyrillanskiy
 * @since 08.02.2019, 21:35.
 */
interface LocationUseCase {

    fun fetchLocation(
        language: Language,
        cellId: Int? = null,
        lac: Int? = null,
        simOperator: String? = null
    ): Single<LocationResponse>

    fun fetchLocation(
        language: Language,
        cellId: Int? = null,
        lac: Int? = null,
        countryCode: Int? = null,
        operatorCode: Int? = null
    ): Single<LocationResponse>

}