package presentation.common

/**
 * Интерфейс для создания состояний в конечном автомате UI.
 *
 * @author Keyrillanskiy
 * @since 06.02.2019, 16:30.
 */
interface FiniteState<E> {
    fun getNextState(event: E): FiniteState<E>
}