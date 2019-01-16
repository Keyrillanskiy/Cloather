package data.repositories.interfaces

import domain.models.responses.TokenWrapper
import domain.models.responses.User
import io.reactivex.Single

/**
 * Репозиторий для работы с пользователем
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:58.
 */
interface UserRepository {

    fun authorize(token: TokenWrapper): Single<User>

}