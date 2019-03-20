package presentation.common

/**
 * Базовый интерфейс для взаимодействия фрагмента с активити.
 *
 * @author Keyrillanskiy
 * @since 13.03.2019, 9:04.
 */
interface ActivityInteractor {
    fun onShowLoading()
    fun onHideLoading()
    fun onInternetError()
}