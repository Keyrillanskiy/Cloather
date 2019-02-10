package domain.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Модель геолокации, полученной через запрос к API.
 * Строки локализованы в зависимости от языка приложения.
 *
 *
 * @author Keyrillanskiy
 * @since 08.02.2019, 17:42.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationResponse(
    @JsonProperty(value = "position") val position: Position
) {
    /**
     * @param latitude Широта
     * @param longitude Долгота
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Position(
        @JsonProperty(value = "latitude", required = true) val latitude: Double,
        @JsonProperty(value = "longitude", required = true) val longitude: Double
    )
}