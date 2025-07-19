package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Success(val player: Player) : LoginState
    data class Error(val message: String) : LoginState
}

class LoginViewModel : ViewModel() {
    private val backend = BackendAPI()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun loginUser(name: String, password: String) {
        if (name.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Benutzername und Passwort eingeben!")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _loginState.value = LoginState.Loading
            try {
                val player = backend.authenticate(name, password)
                _loginState.value = LoginState.Success(player)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Amneldung fehlgeschlagen - Name oder Passwort falsch.")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}