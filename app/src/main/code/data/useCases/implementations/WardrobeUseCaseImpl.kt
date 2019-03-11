package data.useCases.implementations

import data.repositories.interfaces.WardrobeRepository
import data.useCases.interfaces.WardrobeUseCase
import domain.models.entities.ThingQuery
import domain.models.responses.Category
import domain.models.responses.Thing
import domain.models.values.Gender
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 04.03.2019, 10:37.
 */
class WardrobeUseCaseImpl(private val wardrobeRepository: WardrobeRepository) : WardrobeUseCase {

    override fun fetchWhatToWear(latitude: Double, longitude: Double, token: String): Single<List<Thing>> {
        return wardrobeRepository.fetchWhatToWear(latitude, longitude, token)
    }

    override fun fetchCaterories(gender: Gender): Single<List<Category>> {
        return wardrobeRepository.fetchCaterories(gender)
    }

    override fun fetchUsersWardrobe(id: String?): Single<List<Thing>> {
        return wardrobeRepository.fetchUsersWardrobe(id)
    }

    override fun fetchThingsForWardrobeScreen(categoryId: String, gender: Gender): Single<List<Thing>> {
        val query = ThingQuery()
        query.addGenderFilter(gender)
        query.addCategoryFilter(categoryId)
        return wardrobeRepository.fetchThingsByCriteria(query.filter)
    }

    override fun addThingToWardrobe(uid: String, thingId: String): Completable {
        return wardrobeRepository.addThingToWardrobe(uid, thingId)
    }

    override fun deleteThingFromWardrobe(uid: String, thingId: String): Completable {
        return wardrobeRepository.deleteThingFromWardrobe(uid, thingId)
    }

}