package domain.models.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Модели для работы с одеждой
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 20:25.
 */

data class Category(
    val id: String,
    val name: String,
    val priority: Int,
    @JsonProperty("imageLink") val imageUrl: String?
)

data class CategoryItem(var id: String, var name: String, var imageUrl: String?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Thing(
    val id: String,
    val name: String,
    val priority: Int,
    val onPersonImage: String?,
    val previewImage: String?,
    @JsonProperty("onModel") val modelImages: ModelImages?,
    @JsonProperty("onPreview") val previewImages: PreviewImages?
)

//TODO: перенести в другое место на уровень ui
data class ThingItem(
    var id: String,
    var name: String,
    var imageUrl: String?,
    var isChecked: Boolean
)

data class ModelImages(
    @JsonProperty("man") val manImageUrl: String?,
    @JsonProperty("woman") val womanImageUrl: String?
)

data class PreviewImages(
    @JsonProperty("man") val manImageUrl: String?,
    @JsonProperty("woman") val womanImageUrl: String?
)
