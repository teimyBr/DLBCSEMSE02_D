package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import com.boardgamer.model.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

sealed interface SaveState {
    data object Idle : SaveState
    data object Loading : SaveState
    data object Success : SaveState
    data class Error(val messageResId: Int) : SaveState
}

class NewAppointmentViewModel : ViewModel() {

    companion object {
        const val SCREEN_NAME = "NewAppointment"
    }

    private val backend = BackendAPI()

    private val _date = MutableStateFlow("")
    val date = _date.asStateFlow()

    private val _time = MutableStateFlow("")
    val time = _time.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes = _notes.asStateFlow()

    private val _isLocationDifferent = MutableStateFlow(false)
    val isLocationDifferent = _isLocationDifferent.asStateFlow()

    private val _customLocation = MutableStateFlow("")
    val customLocation = _customLocation.asStateFlow()

    private val _hostLocation = MutableStateFlow("")
    val hostLocation = _hostLocation.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()

    init {
        _hostLocation.value = SessionManager.currentPlayer?.location ?: "Ort des Gastgebers"
    }

    fun onDateChange(newDate: String) { _date.value = newDate }
    fun onTimeChange(newTime: String) { _time.value = newTime }
    fun onNotesChange(newNotes: String) { _notes.value = newNotes }
    fun onLocationSwitchChange(isChecked: Boolean) {
        _isLocationDifferent.value = isChecked
        if (!isChecked) {
            _customLocation.value = ""
        }
    }    fun onCustomLocationChange(newLocation: String) { _customLocation.value = newLocation }

    fun saveAppointment() {
        viewModelScope.launch(Dispatchers.IO) {
            _saveState.value = SaveState.Loading

            val hostId = SessionManager.currentPlayer?.id
            if (hostId == null) {
                _saveState.value = SaveState.Error(R.string.error_no_user_logged_in)
                return@launch
            }

            val finalLocation = if (_isLocationDifferent.value) {
                _customLocation.value
            } else {
                _hostLocation.value
            }

            if (finalLocation.isBlank()) {
                _saveState.value = SaveState.Error(R.string.error_location_empty)
                return@launch
            }

            try {
                val parsedDate = parseDate(_date.value)
                val parsedTime = parseTime(_time.value)
                val parsedTimestamp = LocalDateTime(parsedDate, parsedTime)

                val newAppointment = Appointment(
                    date = parsedDate,
                    timestamp = parsedTimestamp,
                    location = finalLocation,
                    hostId = hostId
                )

                val result = backend.addAppointment(newAppointment)
                if (result > -1) {
                    _saveState.value = SaveState.Success
                } else {
                    _saveState.value = SaveState.Error(R.string.error_saving_appointment)
                }

            } catch (e: Exception) {
                _saveState.value = SaveState.Error(R.string.error_invalid_date_time)
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }

    private fun parseDate(dateStr: String): LocalDate {
        val parts = dateStr.split(".").map { it.toInt() }
        return LocalDate(parts[2], parts[1], parts[0])
    }

    private fun parseTime(timeStr: String): LocalTime {
        val parts = timeStr.split(":").map { it.toInt() }
        return LocalTime(parts[0], parts[1])
    }
}