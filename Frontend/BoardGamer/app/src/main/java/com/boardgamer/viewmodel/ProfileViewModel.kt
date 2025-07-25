package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userName: String = "",
    val playerGroup: List<Player> = emptyList(),
    val loggedOut: Boolean = false
)

class ProfileViewModel(private val playerId: Long) : ViewModel() {
    companion object {
        const val SCREEN_NAME = "Profile"
    }

    private val backend = BackendAPI()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            val currentUser = backend.getPlayer(playerId)
            val allPlayers = backend.getPlayers()

            if (currentUser != null) {
                _uiState.update {
                    it.copy(
                        userName = currentUser.name,
                        playerGroup = allPlayers
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.update { it.copy(loggedOut = true) }
    }
}

class ProfileViewModelFactory(private val playerId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(playerId) as T
        }
        throw IllegalArgumentException("Unbekanntes ViewModel")
    }
}