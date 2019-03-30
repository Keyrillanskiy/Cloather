package data.network.apiSpecs

import domain.models.responses.Category
import domain.models.responses.Thing
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Интерфейс для запросов, связанных с гардеробом
 *
 * @author Keyrillanskiy
 * @since 15.01.2019, 21:33.
 */
interface WardrobeApiSpec {

    @GET("/api/things/{id}")
    fun fetchThing(@Path("id") id: String, @Body thingId: RequestBody, @Query("lang") language: String): Single<Thing>

    @GET("/api/things")
    fun fetchThingByCriteria(@QueryMap filters: Map<String, String?>, @Query("lang") language: String): Single<List<Thing>>

    @GET("/api/categories")
    fun fetchCategories(@Query("gender") gender: String, @Query("lang") language: String): Single<List<Category>>

    @GET("/api/categories/{id}")
    fun fetchCategory(@Path("id") categoryId: String, @Query("lang") language: String): Single<Category>

    @GET("/api/whattowear")
    fun fetchWhatToWear(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("token") token: String,
        @Query("lang") language: String
    ): Single<List<Thing>>

    @PUT("/api/user/{id}/defaultwardrobe")
    fun setGenderForDefaultWardrobe(@Path("id") id: String, @Body gender: RequestBody): Single<ResponseBody>

    @PUT("/api/user/{id}/wardrobe")
    fun addThingToWardrobe(@Path("id") id: String, @Body thingId: RequestBody): Completable

    @HTTP(method = "DELETE", path = "/api/user/{id}/wardrobe", hasBody = true)
    fun deleteThingFromWardrobe(@Path("id") id: String, @Body thingId: RequestBody): Completable

    @GET("/api/user/{id}/wardrobe")
    fun fetchUsersWardrobe(@Path("id") id: String?): Single<List<Thing>>

}