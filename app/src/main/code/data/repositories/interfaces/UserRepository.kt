package data.repositories.interfaces

import domain.models.responses.TokenWrapper
import domain.models.responses.UserResponse
import io.reactivex.Single

/**
 * Репозиторий для работы с пользователем
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:58.
 */
interface UserRepository {

    /**
     * Функция для авторизации на сервере через google googleToken
     *
     * @param token Токен google
     *
     * @return [Single]<[UserResponse]> данные пользователя (новые, либо существующие)
     */
    fun authorize(token: TokenWrapper): Single<UserResponse>

}