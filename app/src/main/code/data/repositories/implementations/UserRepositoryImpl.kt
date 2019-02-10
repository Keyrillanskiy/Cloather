package data.repositories.implementations

import data.network.CloatherHttpClient
import data.network.apiSpecs.UserApiSpec
import data.repositories.interfaces.UserRepository
import domain.models.responses.TokenWrapper
import domain.models.responses.UserResponse
import domain.models.values.Gender
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:59.
 */
class UserRepositoryImpl(httpClient: CloatherHttpClient) : UserRepository {

    private val userApi = httpClient.userApi

    override fun authorize(token: TokenWrapper): Single<UserResponse> = userApi.authorize(token)

    override fun setGender(userId: String, gender: Gender): Completable {
        val genderRequestBody = RequestBody.create(MediaType.parse("text/plain"), gender.value.toString())
        return userApi.setGender(userId, genderRequestBody)
    }

}