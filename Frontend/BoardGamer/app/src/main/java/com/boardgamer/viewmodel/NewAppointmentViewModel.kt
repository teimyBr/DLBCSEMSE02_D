package com.boardgamer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
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
        const val TAG = "NewAppointmentVM"
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

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()

    private var hostId: Long = -1

    fun initialize(playerId: Long) {
        this.hostId = playerId
    }

    fun onDateChange(newDate: String) {
        _date.value = newDate
    }

    fun onTimeChange(newTime: String) {
        _time.value = newTime
    }

    fun onNotesChange(newNotes: String) {
        _notes.value = newNotes
    }

    fun onLocationSwitchChange(isChecked: Boolean) {
        _isLocationDifferent.value = isChecked
        if (!isChecked) {
            _customLocation.value = ""
        }
    }

    fun onCustomLocationChange(newLocation: String) {
        _customLocation.value = newLocation
    }

    fun saveAppointment() {
        viewModelScope.launch(Dispatchers.IO) {
            _saveState.value = SaveState.Loading

            if (hostId == -1L) {
                _saveState.value = SaveState.Error(R.string.error_saving_appointment)
                return@launch
            }

            val host = backend.getPlayer(hostId)

            val finalLocation = if (_isLocationDifferent.value) {
                _customLocation.value
            } else {
                host?.location ?: ""
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
                Log.e(TAG, "Error", e)
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