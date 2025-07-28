package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class NewMessageViewModel : ViewModel() {
    companion object {
        const val SCREEN_NAME = "NewMessage"
    }

    private val backend = BackendAPI()
    private var currentPlayerId: Long = -1L
    private var currentAppointmentId: Long = -1L

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()

    fun initialize(playerId: Long, appointmentId: Long) {
        this.currentPlayerId = playerId
        this.currentAppointmentId = appointmentId
    }

    fun onMessageChange(text: String) {
        _messageText.value = text
    }

    fun postMessage(message: String) {
        if (message.isBlank()) return

        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            val timestamp = LocalDateTime.parse(java.time.LocalDateTime.now().toString())

            val messageObject = Message(
                message = message,
                playerId = currentPlayerId,
                appointmentId = currentAppointmentId,
                timestamp = timestamp
            )
            val result = backend.addMessage(messageObject)
            _saveState.value = if (result > -1) SaveState.Success else SaveState.Error(0)
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }
}