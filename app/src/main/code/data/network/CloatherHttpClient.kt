package data.network

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.keyrillanskiy.cloather.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import utils.serverBaseUrl
import java.util.concurrent.TimeUnit

/**
 * Класс для конфигурации работы с http
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 18:00.
 */
class CloatherHttpClient {

    companion object {
        private const val TIMEOUT_SEC = 30L
        private const val TIMEOUT_READ_SEC = 30L
        private const val TIMEOUT_WRITE_SEC = 2 * 30L
    }

    val client by lazy { configRetrofit(serverBaseUrl, BuildConfig.DEBUG) }

    private fun configRetrofit(baseUrl: String, isDebugging: Boolean): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .client(configHttpClient(isDebugging))
            .build()
    }

    private fun configHttpClient(isDebugging: Boolean): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE_SEC, TimeUnit.SECONDS)
            .addInterceptor(configLoggingInterceptor(isDebugging))
            .build()
    }

}

fun configLoggingInterceptor(isDebugging: Boolean): Interceptor {
    return HttpLoggingInterceptor().apply {
        level = if (isDebugging) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}