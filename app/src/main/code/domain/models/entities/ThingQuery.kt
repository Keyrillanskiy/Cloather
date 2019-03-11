package domain.models.entities

import domain.models.values.Gender
import java.util.*

/**
 * @author Keyrillanskiy
 * @since 04.03.2019, 10:33.
 */
class ThingQuery {

    val filter: MutableMap<String, String> = HashMap()

    fun addCategoryFilter(categoryId: String) {
        filter["category"] = categoryId
    }

    fun addGenderFilter(gender: Gender) {
        filter["gender"] = gender.value.toString()
    }

    fun addMinTempFilter(minT: Int?) {
        filter["minT"] = minT.toString()
    }

    fun addMaxTempFilter(maxT: Int?) {
        filter["maxT"] = maxT.toString()
    }

    fun addWaterResFilter(waterRes: Boolean?) {
        filter["waterRes"] = waterRes.toString()
    }

    fun addWindResFilter(windRes: Boolean?) {
        filter["windRes"] = windRes.toString()
    }

}