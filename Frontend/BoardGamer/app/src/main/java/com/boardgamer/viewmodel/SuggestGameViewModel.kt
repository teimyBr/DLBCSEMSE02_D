package com.boardgamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.DateTimeFormats
import com.boardgamer.model.GameSuggestion
import com.boardgamer.ui.toJavaLocalDate
import com.boardgamer.ui.toJavaLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SuggestGameViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val SCREEN_NAME = "SuggestGame"
    }

    val backend = BackendAPI()
    private var appointmentId by Delegates.notNull<Long>()

    private val gameListFlow = MutableStateFlow<List<GameSuggestionItem>>(listOf())
    val gameList = gameListFlow.asStateFlow()

    private val subtitleFlow = MutableStateFlow("")
    val subtitle = subtitleFlow.asStateFlow()

    private val saveStateFlow = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = saveStateFlow.asStateFlow()

    fun initialize(appointmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            this@SuggestGameViewModel.appointmentId = appointmentId
            val appointment = backend.getAppointments().first { it.id == appointmentId }
            val host = backend.getPlayer(appointment.hostId)!!
            val subtitle =
                getApplication<Application>().applicationContext.getString(
                    R.string.suggest_game_subtitle,
                    appointment.date.toJavaLocalDate().format(DateTimeFormats.dateFormatter),
                    appointment.timestamp.toJavaLocalDateTime()
                        .format(DateTimeFormats.timeFormatter),
                    host.name
                )
            subtitleFlow.emit(subtitle)
            loadGameList()
        }
    }

    suspend fun loadGameList() {
        val suggestions = backend.getGameSuggestions(appointmentId).map { it.gameId }
        val games = backend.getGames().filter { it.id !in suggestions }
        gameListFlow.emit(games.map { GameSuggestionItem(it) })
    }

    fun suggest() {
        viewModelScope.launch(Dispatchers.IO) {
            val gameSuggestions = gameList.value.filter { it.selected.value }.map {
                GameSuggestion(
                    gameId = it.game.id,
                    appointmentId = appointmentId
                )
            }

            if (gameSuggestions.isNotEmpty()) {
                val success = backend.addGameSuggestions(gameSuggestions)

                if (success) saveStateFlow.emit(SaveState.Success)
                else saveStateFlow.emit(
                    SaveState.Error(0)
                )
            } else {
                saveStateFlow.emit(SaveState.Success)
            }
        }
    }

    fun resetSaveState() {
        saveStateFlow.value = SaveState.Idle
    }
}