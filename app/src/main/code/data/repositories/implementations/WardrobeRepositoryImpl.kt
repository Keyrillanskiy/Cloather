package data.repositories.implementations

import data.network.CloatherHttpClient
import data.repositories.interfaces.WardrobeRepository
import domain.models.responses.Thing
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 10.02.2019, 17:20.
 */
class WardrobeRepositoryImpl(httpClient: CloatherHttpClient): WardrobeRepository {

    private val wardrobeApi = httpClient.wardrobeApi

    override fun fetchWhatToWear(latitude: Double, longitude: Double, token: String): Single<List<Thing>> {
        return wardrobeApi.fetchWhatToWear(latitude, longitude, token)
    }

}