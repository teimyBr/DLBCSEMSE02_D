package com.boardgamer.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppointmentDetails(
    val appointment: Appointment,
    val hostName: String
)

sealed interface AppointmentsState {
    data object Loading : AppointmentsState
    data class Success(
        val appointments: List<AppointmentDetails>,
        val isNextHost: Boolean
    ) : AppointmentsState
    data class Error(@StringRes val messageResId: Int) : AppointmentsState
}

class CurrentEventsViewModel(private val playerId: Long) : ViewModel() {
    companion object {
        const val SCREEN_NAME = "CurrentEvents"
    }

    private val backend = BackendAPI()

    private val _appointmentsState = MutableStateFlow<AppointmentsState>(AppointmentsState.Loading)
    val appointmentsState: StateFlow<AppointmentsState> = _appointmentsState.asStateFlow()

    init {
        loadAppointmentsWithHostNames()
    }

    fun refreshAppointments() {
        loadAppointmentsWithHostNames()
    }

    private fun loadAppointmentsWithHostNames() {
        viewModelScope.launch(Dispatchers.IO) {
            _appointmentsState.value = AppointmentsState.Loading
            try {
                val appointmentsDeferred = async { backend.getAppointments() }
                val isNextHostDeferred = async { backend.isNextHost(playerId) }
                val appointments = appointmentsDeferred.await()
                val isNextHost = isNextHostDeferred.await()

                val appointmentDetails = appointments.map { appointment ->
                    async {
                        val player = backend.getPlayer(appointment.hostId)
                        AppointmentDetails(
                            appointment = appointment,
                            hostName = player?.name ?: "Unbekannt"
                        )
                    }
                }.awaitAll()

                _appointmentsState.value = AppointmentsState.Success(
                    appointments = appointmentDetails,
                    isNextHost = isNextHost
                )
            } catch (e: Exception) {
                _appointmentsState.value =
                    AppointmentsState.Error(R.string.error_loading_appointments)
            }
        }
    }
}

class CurrentEventsViewModelFactory(private val playerId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentEventsViewModel::class.java)) {
            return CurrentEventsViewModel(playerId) as T
        }
        throw IllegalArgumentException("Unbekanntes ViewModel")
    }
}