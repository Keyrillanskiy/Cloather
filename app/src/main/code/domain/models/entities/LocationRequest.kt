package domain.models.entities

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import domain.models.values.Language
import utils.locatorApiKey
import utils.locatorApiVersion

/**
 * @author Keyrillanskiy
 * @since 08.02.2019, 21:40.
 */
data class LocationRequest(
    val language: Language,
    val cellId: Int?,
    val lac: Int?,
    val countryCode: Int?,
    val operatorId: Int?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LocationRequestJson(
    @JsonProperty(value = "common") val common: Common,
    @JsonProperty(value = "gsm_cells", required = false) val gsmCells: List<GsmCell>?
) {
    data class Common(
        @JsonProperty(value = "version") val version: String = locatorApiVersion,
        @JsonProperty(value = "api_key") val apiKey: String = locatorApiKey
    )

    data class GsmCell(
        @JsonProperty(value = "countrycode") val countryCode: Int,
        @JsonProperty(value = "operatorid") val operatorId: Int,
        @JsonProperty(value = "cellid") val cellId: Int,
        @JsonProperty(value = "lac") val lac: Int
    )
}