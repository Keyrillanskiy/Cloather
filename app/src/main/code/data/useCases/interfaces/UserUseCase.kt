package data.useCases.interfaces

import domain.models.entities.User
import domain.models.responses.TokenWrapper
import domain.models.values.Gender
import io.reactivex.Completable
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

    /**
     * Функция для изменения пола пользователя
     *
     * @param userId id пользователя
     * @param gender новое значение пола
     *
     * @return [Completable] для информирования о завершении операции
     */
    fun setGender(userId: String, gender: Gender): Completable

    /**
     * Функция для получения пользователя.
     *
     * @return Данные пользователя.
     */
    fun fetchUser(): Single<User>

}