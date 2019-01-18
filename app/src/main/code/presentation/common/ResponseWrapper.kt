package presentation.common

/**
 * Класс для оборачивания данных, которые приходят из ViewModel.
 * Таким образом данные "приобретают 3 состояния": успешно (success),
 * ошибка (failure) и загрузка (loading)
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:52.
 */

@Suppress("unused")
sealed class Response<out T> {
    companion object
}

data class Success<out T>(val value: T) : Response<T>()

data class Failure<out T>(val error: Throwable) : Response<T>()

class Loading<out T> : Response<T>()

fun <T> Response.Companion.success(value: T) = Success(value)

fun <T> Response.Companion.failure(error: Throwable) = Failure<T>(error)

fun <T> Response.Companion.loading() = Loading<T>()