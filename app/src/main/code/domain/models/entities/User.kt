package domain.models.entities

import domain.models.values.Gender
import domain.models.values.PreferredWeather

/**
 * Модель пользователя
 *
 * @author Keyrillanskiy
 * @since 18.01.2019, 20:49.
 */
data class User(
    val id: String?,
    val name: String?,
    val gender: Gender,
    val preferredWeather: PreferredWeather,
    val token: String?,
    val googleAvatarURL: String?
)