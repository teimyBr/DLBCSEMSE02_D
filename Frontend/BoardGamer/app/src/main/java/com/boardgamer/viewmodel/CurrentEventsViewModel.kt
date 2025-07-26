package com.boardgamer.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import com.boardgamer.model.SessionManager
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

    var isNextHost = false

    init {
        loadAppointmentsWithHostNames()
        viewModelScope.launch(Dispatchers.IO) {
            isNextHost = backend.isNextHost(SessionManager.currentPlayer.id)
        }
    }

    fun refreshAppointments() {
        loadAppointmentsWithHostNames()
    }

    private fun loadAppointmentsWithHostNames() {
        viewModelScope.launch(Dispatchers.IO) {
            val playerAppointments = backend.getPlayerAppointments()
            _appointmentsState.value = AppointmentsState.Loading
            try {
                val appointments = backend.getAppointments()
                val appointmentDetails = appointments.map { appointment ->
                    val player = backend.getPlayer(appointment.hostId)
                    val canParticipate = SessionManager.currentPlayer.id != appointment.hostId
                            && playerAppointments.none { it.playerId == SessionManager.currentPlayer.id && it.appointmentId == appointment.id }
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