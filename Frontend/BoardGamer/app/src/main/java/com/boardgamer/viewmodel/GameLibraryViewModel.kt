package com.boardgamer.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameLibraryViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val SCREEN_NAME = "GameLibrary"
    }

    val backend = BackendAPI()
    private val games = MutableStateFlow<List<Game>>(listOf())
    val gameFlow = games.asStateFlow()

    private val newGameNameFlow = MutableStateFlow("")
    val newGameName = newGameNameFlow.asStateFlow()

    private val newGameDescFlow = MutableStateFlow("")
    val newGameDesc = newGameDescFlow.asStateFlow()

    private val dialogOpenFlow = MutableStateFlow(false)
    val dialogOpen = dialogOpenFlow.asStateFlow()

    private val commitEnabledFlow = MutableStateFlow(false)
    val commitEnabled = commitEnabledFlow.asStateFlow()

    init {
        updateGames()
    }

    private fun updateGames() {
        viewModelScope.launch(Dispatchers.IO) {
            games.value = backend.getGames()
        }
    }

    fun nameChange(value: String) {
        newGameNameFlow.value = value
        updateCommitEnabled()
    }

    fun descriptionChange(value: String) {
        newGameDescFlow.value = value
        updateCommitEnabled()
    }

    fun commitNewGame() {
        dialogOpenFlow.value = false
        viewModelScope.launch(Dispatchers.IO) {
            val newGame = Game(name = newGameName.value, description = newGameDesc.value)
            val id = backend.addGame(newGame)

            if (id < 0) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication<Application>().applicationContext,
                        R.string.add_game_failure,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                games.value = backend.getGames()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        getApplication<Application>().applicationContext,
                        R.string.add_game_success,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    fun dissmisNewGame() {
        dialogOpenFlow.value = false
    }

    fun addGame() {
        newGameNameFlow.value = ""
        newGameDescFlow.value = ""
        dialogOpenFlow.value = true
    }

    fun updateCommitEnabled() {
        commitEnabledFlow.value = newGameName.value.isNotEmpty() && newGameDesc.value.isNotEmpty()
    }
}