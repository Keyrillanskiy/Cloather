package data.useCases.interfaces

import domain.models.entities.User
import domain.models.responses.TokenWrapper
import io.reactivex.Single

/**
 * UseCase для работы с пользователем
 *
 * @author Keyrillanskiy
 * @since 18.01.2019, 21:03.
 */
interface UserUseCase {

    /**
     * Функция для авторизации на сервере через googleToken
     *
     * @param token Токен google
     *
     * @return [Single]<[User]> данные пользователя (новые, либо существующие)
     */
    fun authorize(token: TokenWrapper): Single<User>

}