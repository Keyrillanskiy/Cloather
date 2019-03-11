package data.useCases.interfaces

import domain.models.responses.Category
import domain.models.responses.Thing
import domain.models.values.Gender
import io.reactivex.Completable
import io.reactivex.Single

/**
 * UseCase для работы с гардеробом пользователя.
 *
 * @author Keyrillanskiy
 * @since 04.03.2019, 10:35.
 */
interface WardrobeUseCase {

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
     * Функция для получения списка вещей для данной категории.
     *
     * @param categoryId Id категории.
     * @param gender Пол пользователя.
     *
     * @return Список вещей.
     */
    fun fetchThingsForWardrobeScreen(categoryId: String, gender: Gender): Single<List<Thing>>

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