package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.FoodDirection
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
    private val backend = BackendAPI()

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()

    val foodDirections: List<FoodDirection> = listOf(
        FoodDirection(id = 1, designation = "Italienisch"),
        FoodDirection(id = 2, designation = "Griechisch"),
        FoodDirection(id = 3, designation = "Deutsch"),
        FoodDirection(id = 4, designation = "Türkisch"),
        FoodDirection(id = 5, designation = "Amerikanisch"),
        FoodDirection(id = 6, designation = "Asiatisch"),
        FoodDirection(id = 7, designation = "Egal")
    )

    fun registerUser(name: String, email: String, password: String, favouriteFoodId: Long, location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _registrationState.value = RegistrationState.Loading
            try {
                val player = RegistrationPlayer(
                    name = name,
                    email = email,
                    password = password,
                    favouriteFoodId = favouriteFoodId,
                    location = location
                )
                val newId = backend.register(player)

                if (newId != -1L) {
                    _registrationState.value = RegistrationState.Success(newId)
                } else {
                    _registrationState.value = RegistrationState.Error("Die Registrierung ist fehlgeschlagen.")
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