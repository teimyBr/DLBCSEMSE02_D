package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
Noch zu erledigen:
- Error-String anpassen

 */

sealed interface AppointmentsState {
    data object Loading : AppointmentsState
    data class Success(val appointments: List<Appointment>) : AppointmentsState
    data class Error(val message: String) : AppointmentsState
}

class CurrentEventsViewModel : ViewModel() {
    companion object {
        const val SCREEN_NAME = "CurrentEvents"
    }

    private val backend = BackendAPI()

    private val _appointmentsState = MutableStateFlow<AppointmentsState>(AppointmentsState.Loading)
    val appointmentsState = _appointmentsState.asStateFlow()

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        viewModelScope.launch(Dispatchers.IO) {
            _appointmentsState.value = AppointmentsState.Loading
            try {
                val appointments = backend.getAppointments()
                _appointmentsState.value = AppointmentsState.Success(appointments)
            } catch (e: Exception) {
                _appointmentsState.value = AppointmentsState.Error("Fehler beim Laden der Termine.")
            }
        }
    }
}