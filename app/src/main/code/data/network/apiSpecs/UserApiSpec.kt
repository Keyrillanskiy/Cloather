package data.network.apiSpecs

import domain.models.responses.TokenWrapper
import domain.models.responses.UserResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Интерфейс для запросов, связанных с пользователем
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 21:32.
 */
interface UserApiSpec {
    @POST("/api/user/auth")
    fun authorize(@Body token: TokenWrapper): Single<UserResponse>

    @GET("/api/user/{id}")
    fun fetchUserData(@Path("id") id: String): Single<UserResponse>

    @PUT("/api/user/{id}/gender")
    fun setGender(@Path("id") id: String, @Body gender: RequestBody): Completable

    @PUT("/api/user/{id}/weather")
    fun setPrefWeather(@Path("id") id: String, @Body prefWeather: RequestBody): Completable
}