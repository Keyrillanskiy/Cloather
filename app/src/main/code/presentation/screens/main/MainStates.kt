package presentation.screens.main

import presentation.common.FiniteState

/**
 * Набор состояний главного экрана, события переходов между ними и связи состояний.
 *
 * @author Keyrillanskiy
 * @since 04.02.2019, 12:59.
 */

sealed class Event {
    object RequestLocationPermission : Event()
    object LocationPermissionGranted : Event()
    object LocationPermissionAlreadyGranted : Event()
    object LocationPermissionRejected : Event()
    object LocationDisabled : Event()
    object LocationEnabled : Event()
    object LocationPermissionRemoved : Event()
    object InternetDisabled : Event()
    object InternetEnabled : Event()
    object FetchLocation : Event()
    data class FetchLocationError(val t: Throwable? = null) : Event()
    data class FetchData(val latitude: Double, val longitude: Double) : Event()
    object DataFetched : Event()
    data class DataFetchError(val t: Throwable) : Event()
    object DataFetchErrorHandled : Event()
}

// initial state
class WithoutLocationPermissionState : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.LocationPermissionAlreadyGranted -> WithoutLocationPermissionState()
            is Event.RequestLocationPermission -> LocationPermissionRequestedState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class LocationPermissionRequestedState : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.LocationPermissionGranted -> WithLocationPermissionState()
            is Event.LocationPermissionRejected -> WithoutLocationPermissionState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class WithLocationPermissionState : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.LocationDisabled -> WithLocationPermissionState()
            is Event.LocationEnabled -> LocationEnabledState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class LocationEnabledState : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.InternetDisabled -> LocationEnabledState()
            is Event.InternetEnabled -> ReadyToFetchDataState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class ReadyToFetchDataState : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.InternetDisabled -> ReadyToFetchDataState()
            is Event.FetchLocation -> LoadingGeolocationState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class DataFetchErrorState(val throwable: Throwable?) : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.DataFetchErrorHandled -> ReadyToFetchDataState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class LoadingGeolocationState : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.FetchData -> UpdatingDataState(event.latitude, event.longitude)
            is Event.FetchLocationError -> DataFetchErrorState(event.t)
            is Event.LocationDisabled -> WithLocationPermissionState()
            is Event.LocationPermissionRemoved -> WithoutLocationPermissionState()
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}

class UpdatingDataState(val latitude: Double, val longitude: Double) : FiniteState<Event> {
    override fun performTransition(event: Event): FiniteState<Event> {
        return when (event) {
            is Event.DataFetched -> ReadyToFetchDataState()
            is Event.DataFetchError -> DataFetchErrorState(event.t)
            else -> throw IllegalStateException("Invalid event $event passed to state $this")
        }
    }
}