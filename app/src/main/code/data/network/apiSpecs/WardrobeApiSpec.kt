package data.network.apiSpecs

import domain.models.responses.Category
import domain.models.responses.Thing
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
    fun fetchThing(@Path("id") id: String, @Body thingId: RequestBody): Single<Thing>

    @GET("/api/things")
    fun fetchThingByCriteria(@QueryMap filters: Map<String, String?>): Single<List<Thing>>

    @GET("/api/categories")
    fun fetchCategories(@Query("gender") gender: String?): Single<List<Category>>

    @GET("/api/categories/{id}")
    fun fetchCategory(@Path("id") categoryId: String): Single<Category>

    @GET("/api/whattowear")
    fun fetchWhatToWear(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("googleToken") token: String?
    ): Single<List<Thing>>

    @PUT("/api/user/{id}/defaultwardrobe")
    fun setGenderForDefaultWardrobe(@Path("id") id: String, @Body gender: RequestBody): Single<ResponseBody>

    @PUT("/api/user/{id}/wardrobe")
    fun addThingToWardrobe(@Path("id") id: String, @Body thingId: RequestBody): Single<ResponseBody>

    @HTTP(method = "DELETE", path = "/api/user/{id}/wardrobe", hasBody = true)
    fun deleteThingFromWardrobe(@Path("id") id: String, @Body thingId: RequestBody): Single<ResponseBody>

    @GET("/api/user/{id}/wardrobe")
    fun fetchUsersWardrobe(@Path("id") id: String?): Single<List<Thing>>

}