package domain.models.responses

import com.google.gson.annotations.SerializedName


/**
 * Модель геолокации, полученной через запрос к API.
 * Строки локализованы в зависимости от языка приложения.
 *
 *
 * @author Keyrillanskiy
 * @since 08.02.2019, 17:42.
 */
data class LocationResponse(
    @SerializedName(value = "position") val position: Position
) {
    /**
     * @param latitude Широта
     * @param longitude Долгота
     */
    data class Position(
        @SerializedName(value = "latitude") val latitude: Double,
        @SerializedName(value = "longitude") val longitude: Double
    )
}