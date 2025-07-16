package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameLibraryViewModel : ViewModel() {
    val backend = BackendAPI()
    private val games = MutableStateFlow<List<Game>>(listOf())
    val gameFlow = games.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            games.value = backend.getGames()
        }
    }
}