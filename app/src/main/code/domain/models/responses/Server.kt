package domain.models.responses

import com.google.gson.annotations.SerializedName

/**
 * Модели для взаимодействия с сервером
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 20:21.
 */

/**
 * Класс для оборачивания токена перед отправкой на сервер
 */
data class TokenWrapper(@SerializedName(value = "googleToken") val token: String)

data class UserResponse(
    val id: String,
    val token: String,
    val name: String,
    val gender: String,
    val preferredWeather: String,
    val googleUid: String,
    val googleEmail: String,
    val googleAvatarURL: String
)