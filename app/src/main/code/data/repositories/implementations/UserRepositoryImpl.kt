package data.repositories.implementations

import data.network.CloatherHttpClient
import data.network.apiSpecs.UserApiSpec
import data.repositories.interfaces.UserRepository
import domain.models.responses.TokenWrapper
import domain.models.responses.UserResponse
import io.reactivex.Single

/**
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:59.
 */
class UserRepositoryImpl(httpClient: CloatherHttpClient) : UserRepository {

    private val userApi = httpClient.client.create(UserApiSpec::class.java)

    override fun authorize(token: TokenWrapper): Single<UserResponse> = userApi.authorize(token)

}