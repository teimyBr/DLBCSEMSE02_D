package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Success(val player: Player) : LoginState
    data class Error(val message: String) : LoginState
}

class HomeViewModel : ViewModel() {

    companion object {
        const val SCREEN_NAME = "Home"
    }

    private val backend = BackendAPI()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    val isLoginButtonEnabled: StateFlow<Boolean> =
        combine(username, password, loginState) { username, password, loginState ->
            username.isNotBlank() && password.isNotBlank() && loginState !is LoginState.Loading
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onUsernameChange(value: String) {
        _username.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun loginPlayer() {
        val name = _username.value
        val password = _password.value

        if (name.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Benutzername und Passwort eingeben!")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _loginState.value = LoginState.Loading
            try {
                val player = backend.authenticate(name, password)
                if (player != null) {
                    _loginState.value = LoginState.Success(player)
                } else {
                    _loginState.value =
                        LoginState.Error("Anmeldung fehlgeschlagen - Name oder Passwort falsch.")
                }
            } catch (e: Exception) {
                _loginState.value =
                    LoginState.Error("Anmeldung fehlgeschlagen - Name oder Passwort falsch.")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}