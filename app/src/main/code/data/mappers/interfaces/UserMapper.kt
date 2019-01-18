package data.mappers.interfaces

import domain.models.entities.User
import domain.models.responses.UserResponse

/**
 * @author Keyrillanskiy
 * @since 18.01.2019, 20:56.
 */
interface UserMapper {

    fun toUser(userResponse: UserResponse): User

}