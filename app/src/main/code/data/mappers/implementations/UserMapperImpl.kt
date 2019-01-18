package data.mappers.implementations

import data.mappers.interfaces.UserMapper
import domain.models.entities.User
import domain.models.responses.UserResponse
import domain.models.values.toGender
import domain.models.values.toPreferredWeather

/**
 * @author Keyrillanskiy
 * @since 18.01.2019, 20:57.
 */
class UserMapperImpl : UserMapper {

    override fun toUser(userResponse: UserResponse): User {
        return User(
            id = userResponse.id,
            name = userResponse.name,
            gender = userResponse.gender.toGender(),
            preferredWeather = userResponse.preferredWeather.toPreferredWeather(),
            token = userResponse.token,
            googleAvatarURL = userResponse.googleAvatarURL
        )
    }

}