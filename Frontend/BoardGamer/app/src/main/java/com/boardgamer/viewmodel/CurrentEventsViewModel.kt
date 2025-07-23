package com.boardgamer.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class AppointmentDetails(
    val appointment: Appointment,
    val hostName: String
)
sealed interface AppointmentsState {
    data object Loading : AppointmentsState
    data class Success(val appointments: List<AppointmentDetails>) : AppointmentsState
    data class Error(@StringRes val messageResId: Int) : AppointmentsState
}

class CurrentEventsViewModel : ViewModel() {
    companion object {
        const val SCREEN_NAME = "CurrentEvents"
    }

    private val backend = BackendAPI()

    private val _appointmentsState = MutableStateFlow<AppointmentsState>(AppointmentsState.Loading)
    val appointmentsState = _appointmentsState.asStateFlow()

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
                val appointments = backend.getAppointments()
                val appointmentDetails = appointments.map { appointment ->
                    async {
                        val player = backend.getPlayer(appointment.hostId)
                        AppointmentDetails(
                            appointment = appointment,
                            hostName = player?.name ?: "Unbekannt"
                        )
                    }
                }.awaitAll()
                _appointmentsState.value = AppointmentsState.Success(appointmentDetails)
            } catch (e: Exception) {
                _appointmentsState.value = AppointmentsState.Error(R.string.error_loading_appointments)
            }
        }
    }
}