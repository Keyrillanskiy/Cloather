package data.repositories.interfaces

import domain.models.responses.Category
import domain.models.responses.Thing
import domain.models.values.Gender
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Интерфейс репозитория для работы с одеждой.
 *
 * @author Keyrillanskiy
 * @since 10.02.2019, 17:15.
 */
interface WardrobeRepository {

    /**
     * Функция для получения набора одежды для текущей погоды.
     *
     * @param latitude Широта.
     * @param longitude Долгота.
     * @param token Токен пользователя.
     */
    fun fetchWhatToWear(latitude: Double, longitude: Double, token: String): Single<List<Thing>>

    /**
     * Функция для получения списка категорий одежды
     *
     * @param gender Пол.
     *
     * @return Список категорий.
     */
    fun fetchCaterories(gender: Gender): Single<List<Category>>

    /**
     * Функция для получения гардероба пользователя.
     *
     * @param id Id пользователя.
     *
     * @return Список вещей.
     */
    fun fetchUsersWardrobe(id: String?): Single<List<Thing>>

    /**
     * Функция для получения списка вещей, соответствующих фильтру.
     *
     * @param filter Фильтр.
     *
     * @return Список вещей.
     */
    fun fetchThingsByCriteria(filter: Map<String, String>): Single<List<Thing>>

    /**
     * Функция для добавления вещи в гардероб пользователя.
     *
     * @param uid Id пользователя.
     * @param thingId Id вещи.
     */
    fun addThingToWardrobe(uid: String, thingId: String): Completable

    /**
     * Функция для удаления вещи из гардероба пользователя.
     *
     * @param uid Id пользователя.
     * @param thingId Id вещи.
     */
    fun deleteThingFromWardrobe(uid: String, thingId: String): Completable

}