package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.RegistrationPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RegistrationState {
    data object Idle : RegistrationState
    data object Loading : RegistrationState
    data class Success(val userId: Long) : RegistrationState
    data class Error(val message: String) : RegistrationState
}

class RegistrationViewModel : ViewModel() {

    companion object {
        const val SCREEN_NAME = "Register"
    }

    private val backend = BackendAPI()

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _passwordRepeat = MutableStateFlow("")
    val passwordRepeat = _passwordRepeat.asStateFlow()

    private val _passwordsDoNotMatch = MutableStateFlow(false)
    val passwordsDoNotMatch = _passwordsDoNotMatch.asStateFlow()

    fun onUsernameChange(value: String) {
        _username.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onLocationChange(value: String) {
        _location.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onPasswordRepeatChange(value: String) {
        _passwordRepeat.value = value
    }

    fun submitPlayerRegistration() {
        if (_password.value == _passwordRepeat.value) {
            _passwordsDoNotMatch.value = false
            registerPlayer()
        } else {
            _passwordsDoNotMatch.value = true
        }
    }

    private fun registerPlayer() {
        viewModelScope.launch(Dispatchers.IO) {
            _registrationState.value = RegistrationState.Loading
            try {
                val player = RegistrationPlayer(
                    name = _username.value,
                    email = _email.value,
                    password = _password.value,
                    location = _location.value
                )
                val newId = backend.register(player)

                if (newId != -1L) {
                    _registrationState.value = RegistrationState.Success(newId)
                } else {
                    _registrationState.value =
                        RegistrationState.Error("Die Registrierung ist fehlgeschlagen.")
                }
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error(e.message ?: "Fehler!")
            }
        }
    }

    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}