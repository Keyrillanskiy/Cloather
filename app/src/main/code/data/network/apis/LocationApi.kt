package data.network.apis

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.keyrillanskiy.cloather.BuildConfig
import data.network.apiSpecs.LocationApiSpec
import data.network.configLoggingInterceptor
import domain.models.entities.LocationRequest
import domain.models.entities.LocationRequestJson
import domain.models.responses.LocationResponse
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import okhttp3.*
import utils.locatorApiUrl
import java.io.IOException

/**
 * @author Keyrillanskiy
 * @since 09.02.2019, 14:40.
 */
class LocationApi : LocationApiSpec {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(configLoggingInterceptor(BuildConfig.DEBUG))
            .build()
    }

    //TODO: refactor method
    override fun fetchGeolocation(locationRequestData: LocationRequest): Single<LocationResponse> {
        val objectMapper = jacksonObjectMapper()

        val gmsCells = if (
            locationRequestData.countryCode == null
            || locationRequestData.operatorId == null
            || locationRequestData.cellId == null
            || locationRequestData.lac == null
        )
            null
        else listOf(
            LocationRequestJson.GsmCell(
                locationRequestData.countryCode,
                locationRequestData.operatorId,
                locationRequestData.cellId,
                locationRequestData.lac
            )
        )
        val locationRequestJson = LocationRequestJson(LocationRequestJson.Common(), gmsCells)

        val mediaTypeJson = MediaType.parse("application/json")
        val requestBody = RequestBody.create(
            mediaTypeJson, "json=" + objectMapper.writeValueAsString(locationRequestJson)
        )

        val request = Request.Builder()
            .url(locatorApiUrl)
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        val singleSubject = SingleSubject.create<LocationResponse>()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    try {
                        val objResponse = objectMapper.readValue<LocationResponse>(body.string())
                        singleSubject.onSuccess(objResponse)
                    } catch (t: Throwable) {
                        singleSubject.onError(t)
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) = singleSubject.onError(e)
        })

        return singleSubject
    }

}