package data.repositories.implementations

import data.network.CloatherHttpClient
import data.repositories.interfaces.UserRepository
import domain.models.responses.TokenWrapper
import domain.models.responses.User
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:59.
 */
class UserRepositoryImpl(private val httpClient: CloatherHttpClient) : UserRepository {

    override fun authorize(token: TokenWrapper): Single<User> {
        httpClient.client.
    }

}