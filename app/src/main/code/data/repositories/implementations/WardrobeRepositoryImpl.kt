package data.repositories.implementations

import data.network.CloatherHttpClient
import data.repositories.interfaces.WardrobeRepository
import domain.models.responses.Category
import domain.models.responses.Thing
import domain.models.values.Gender
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * @author Keyrillanskiy
 * @since 10.02.2019, 17:20.
 */
class WardrobeRepositoryImpl(httpClient: CloatherHttpClient) : WardrobeRepository {

    private val wardrobeApi = httpClient.wardrobeApi

    override fun fetchWhatToWear(latitude: Double, longitude: Double, token: String): Single<List<Thing>> {
        return wardrobeApi.fetchWhatToWear(latitude, longitude, token)
    }

    override fun fetchCaterories(gender: Gender): Single<List<Category>> {
        return wardrobeApi.fetchCategories(gender.value.toString())
    }

    override fun fetchUsersWardrobe(id: String?): Single<List<Thing>> {
        return wardrobeApi.fetchUsersWardrobe(id)
    }

    override fun fetchThingsByCriteria(filter: Map<String, String>): Single<List<Thing>> {
        return wardrobeApi.fetchThingByCriteria(filter)
    }

    override fun addThingToWardrobe(uid: String, thingId: String): Completable {
        val thingIdBody = RequestBody.create(MediaType.parse("text/plain"), thingId)
        return wardrobeApi.addThingToWardrobe(uid, thingIdBody)
    }

    override fun deleteThingFromWardrobe(uid: String, thingId: String): Completable {
        val thingIdBody = RequestBody.create(MediaType.parse("text/plain"), thingId)
        return wardrobeApi.deleteThingFromWardrobe(uid, thingIdBody)
    }

}