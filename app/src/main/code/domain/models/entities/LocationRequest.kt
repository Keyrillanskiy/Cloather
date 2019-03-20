package domain.models.entities

import com.google.gson.annotations.SerializedName
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

data class LocationRequestJson(
    @SerializedName(value = "common") val common: Common,
    @SerializedName(value = "gsm_cells") val gsmCells: List<GsmCell>?
) {
    data class Common(
        @SerializedName(value = "version") val version: String = locatorApiVersion,
        @SerializedName(value = "api_key") val apiKey: String = locatorApiKey
    )

    data class GsmCell(
        @SerializedName(value = "countrycode") val countryCode: Int,
        @SerializedName(value = "operatorid") val operatorId: Int,
        @SerializedName(value = "cellid") val cellId: Int,
        @SerializedName(value = "lac") val lac: Int
    )
}