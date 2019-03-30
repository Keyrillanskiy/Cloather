package domain.models.exceptions

/**
 * Ошибки для обработки в UI.
 *
 * @author Keyrillanskiy
 * @since 10.02.2019, 14:49.
 */
sealed class UiException {
    data class FetchLocationException(val throwable: Throwable? = null) : UiException()
    data class FetchDataException(val throwable: Throwable? = null) : UiException()
    data class UnknownException(val throwable: Throwable? = null) : UiException()
}