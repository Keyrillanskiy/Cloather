package domain.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Модели для взаимодействия с сервером
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 20:21.
 */

//TODO: перенести константы в класс с авторизацией
private const val G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me"
private const val USER_INFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile"
private const val EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email"
val SCOPES = "$G_PLUS_SCOPE $USER_INFO_SCOPE $EMAIL_SCOPE"

/**
 * Класс для оборачивания токена перед отправкой на сервер
 */
data class TokenWrapper(@JsonProperty("googleToken") var token: String)

data class User(
    val id: String,
    val googleUid: String,
    val googleAvatarURL: String,
    val facebookUid: String,
    val name: String,
    val gender: String,
    val preferredWeather: String,
    val token: String,
    val wardrobe: List<Thing>
)