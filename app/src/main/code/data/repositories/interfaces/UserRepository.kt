package data.repositories.interfaces

import domain.models.entities.User
import domain.models.responses.TokenWrapper
import domain.models.responses.UserResponse
import domain.models.values.Gender
import io.reactivex.Completable
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
     * По возможности загружает по сети, либо берет из shared preferences.
     *
     * @return Данные пользователя.
     */
    fun fetchUser(): Single<User>

}