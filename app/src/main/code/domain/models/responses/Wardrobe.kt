package domain.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import domain.models.values.Gender
import presentation.screens.wardrobe.CategoryItem
import presentation.screens.wardrobe.ThingItem
import java.lang.IllegalArgumentException

/**
 * Модели для работы с одеждой
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 20:25.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    val id: String,
    val name: String,
    val priority: Int,
    @JsonProperty("imageLink") val imageUrl: String?
)

fun Category.toCategoryItem(): CategoryItem {
    return CategoryItem(id, name, imageUrl)
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Thing(
    val id: String,
    val name: String,
    val priority: Int,
    val onPersonImage: String?,
    @JsonProperty("onModel") val modelImages: ModelImages?,
    @JsonProperty("onPreview") val previewImages: PreviewImages?
)

fun Thing.toThingItem(isInWardrobe: Boolean, gender: Gender): ThingItem {
    return when(gender) {
        Gender.MALE -> ThingItem(id, name, previewImages?.manImageUrl, isChecked = isInWardrobe)
        Gender.FEMALE -> ThingItem(id, name, previewImages?.womanImageUrl, isChecked = isInWardrobe)
        Gender.UNDEFINED -> throw IllegalArgumentException("gender undefined")
    }
}

data class ModelImages(
    @JsonProperty("man") val manImageUrl: String?,
    @JsonProperty("woman") val womanImageUrl: String?
)

data class PreviewImages(
    @JsonProperty("man") val manImageUrl: String?,
    @JsonProperty("woman") val womanImageUrl: String?
)
