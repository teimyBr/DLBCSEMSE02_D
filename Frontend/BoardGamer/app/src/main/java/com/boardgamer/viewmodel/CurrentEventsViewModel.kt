package com.boardgamer.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class AppointmentDetails(
    val appointment: Appointment,
    val hostName: String,
    val canParticipate: Boolean = true,
    private val openDialogFlow: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val openDialog: StateFlow<Boolean> = openDialogFlow.asStateFlow()
) {
    fun updateOpenDialog() {
        openDialogFlow.value = !openDialogFlow.value
    }
}

sealed interface AppointmentsState {
    data object Loading : AppointmentsState
    data class Success(val appointments: List<AppointmentDetails>) : AppointmentsState
    data class Error(@field:StringRes val messageResId: Int) : AppointmentsState
}

class CurrentEventsViewModel : ViewModel() {
    companion object {
        const val SCREEN_NAME = "CurrentEvents"
        const val TAG = "CurrEventsVM"
    }

    private val backend = BackendAPI()

    private val _appointmentsState = MutableStateFlow<AppointmentsState>(AppointmentsState.Loading)
    val appointmentsState = _appointmentsState.asStateFlow()

    var isNextHost by mutableStateOf(false)
        private set

    private var currentPlayerId: Long = -1L

    fun initialize(playerId: Long) {
        if (this.currentPlayerId == playerId && _appointmentsState.value is AppointmentsState.Success) return

        this.currentPlayerId = playerId
        loadAppointmentsWithHostNames()

        viewModelScope.launch(Dispatchers.IO) {
            isNextHost = backend.isNextHost(playerId)
        }
    }

    fun refreshAppointments() {
        if (currentPlayerId != -1L) {
            loadAppointmentsWithHostNames()
        }
    }

    private fun loadAppointmentsWithHostNames() {
        if (currentPlayerId == -1L) return

        viewModelScope.launch(Dispatchers.IO) {
            _appointmentsState.value = AppointmentsState.Loading
            try {
                val playerAppointments = backend.getPlayerAppointments()
                val appointments = backend.getAppointments()
                val appointmentDetails = appointments.map { appointment ->
                    val player = backend.getPlayer(appointment.hostId)
                    val canParticipate = currentPlayerId != appointment.hostId
                            && playerAppointments.none { it.playerId == currentPlayerId && it.appointmentId == appointment.id }
                    AppointmentDetails(
                        appointment = appointment,
                        hostName = player?.name ?: "Unbekannt",
                        canParticipate = canParticipate
                    )
                }
                _appointmentsState.value = AppointmentsState.Success(appointmentDetails)
            } catch (e: Exception) {
                Log.i(TAG, "Something went wrong when loading appointments from backend", e)
                _appointmentsState.value =
                    AppointmentsState.Error(R.string.error_loading_appointments)
            }
        }
    }
}