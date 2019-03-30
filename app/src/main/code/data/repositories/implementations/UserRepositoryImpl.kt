package data.repositories.implementations

import data.mappers.interfaces.UserMapper
import data.network.CloatherHttpClient
import data.preferences.Preferences
import data.repositories.interfaces.UserRepository
import domain.models.entities.User
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
class UserRepositoryImpl(
    httpClient: CloatherHttpClient,
    private val preferences: Preferences,
    private val userMapper: UserMapper
) : UserRepository {

    private val userApi = httpClient.userApi

    override fun authorize(token: TokenWrapper): Single<UserResponse> = userApi.authorize(token)

    override fun setGender(userId: String, gender: Gender): Completable {
        val genderRequestBody = RequestBody.create(MediaType.parse("text/plain"), gender.value.toString())
        return userApi.setGender(userId, genderRequestBody)
    }

    override fun fetchUser(): Single<User> {
        val userId = preferences.fetchUser().id ?: throw IllegalStateException("userId == null")
        return userApi.fetchUserData(userId)
            .toObservable()
            .materialize()
            .map { event ->
                when {
                    event.isOnError -> preferences.fetchUser()
                    else -> {
                        val userResponse = event.value ?: throw IllegalStateException("userResponse == null")
                        userMapper.toUser(userResponse)
                    }
                }
            }
            .firstOrError()

    }

}