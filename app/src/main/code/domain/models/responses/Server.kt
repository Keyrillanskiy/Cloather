package domain.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Модели для взаимодействия с сервером
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 20:21.
 */

/**
 * Класс для оборачивания токена перед отправкой на сервер
 */
data class TokenWrapper(@JsonProperty(value = "googleToken") val token: String)

data class User(
    val id: String,
    val googleUid: String,
    val googleAvatarURL: String,
    val name: String,
    val gender: String,
    val preferredWeather: String,
    val token: String,
    val wardrobe: List<Thing>?
)