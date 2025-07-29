package com.boardgamer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import com.boardgamer.model.DateTimeFormats
import com.boardgamer.model.Game
import com.boardgamer.model.GameSuggestion
import com.boardgamer.model.GameVote
import com.boardgamer.model.Message
import com.boardgamer.model.Player
import com.boardgamer.ui.toJavaLocalDate
import com.boardgamer.ui.toJavaLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameVoteItem(
    val game: Game,
    val gameSuggestion: GameSuggestion,
    val gameVote: GameVote
)

class AppointmentInfosViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val SCREEN_NAME = "Infos"
    }

    val backend = BackendAPI()

    lateinit var loggedInPlayer: Player
    lateinit var appointment: Appointment

    private val participantFlow = MutableStateFlow<List<Player>>(listOf())
    val participants = participantFlow.asStateFlow()

    private val gameSuggestionsFlow = MutableStateFlow<List<GameVoteItem>>(listOf())
    val gameSuggestion = gameSuggestionsFlow.asStateFlow()

    private val messageFlow = MutableStateFlow<List<Message>>(listOf())
    val messages = messageFlow.asStateFlow()

    private val subtitleFlow = MutableStateFlow("")
    val subtitle = subtitleFlow.asStateFlow()

    fun setState(playerId: Long, appointmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            loggedInPlayer = backend.getPlayer(playerId)!!
            appointment = backend.getAppointments().first { it.id == appointmentId }
            loadPlayers()
            loadSuggestions()
            loadMessages()
            setSubtitle()
        }
    }

    suspend fun loadPlayers() {
        val participantIds =
            backend.getPlayerAppointments().filter { it.appointmentId == appointment.id }
                .map { it.playerId }
        //Participants are players that are registered or the host
        val participants =
            backend.getPlayers().filter { it.id in participantIds || it.id == appointment.hostId }

        participantFlow.emit(participants)
    }

    suspend fun loadSuggestions() {
        val suggestions = backend.getGameSuggestions(appointment.id)
        val gameIds = suggestions.map { it.gameId }
        val games = backend.getGames().filter { it.id in gameIds }
        val gameVotes = backend.getGameVotesForPlayer(appointment.id, loggedInPlayer.id)

        val gameVoteItems = mutableListOf<GameVoteItem>()
        for (suggestion in suggestions) {
            var gameVote = gameVotes.find { it.gameSuggestionId == suggestion.id }
            //As game vote has no idea, there is no way to track when to do an insert or an update
            //Consequently add all game votes as soon as the ui is opened the first time
            if (gameVote == null) {
                gameVote = GameVote(loggedInPlayer.id, suggestion.id, false)
                backend.addGameVote(gameVote)
            }
            gameVoteItems.add(
                GameVoteItem(
                    games.first { it.id == suggestion.gameId },
                    suggestion,
                    gameVote
                )
            )
        }
        gameSuggestionsFlow.emit(gameVoteItems)
    }

    suspend fun loadMessages() {
        messageFlow.emit(backend.getMessages(appointment.id))
    }

    suspend fun setSubtitle() {
        val context = getApplication<Application>().applicationContext
        val hostName = backend.getPlayer(appointment.hostId)!!.name
        val string = context.getString(
            R.string.infos_subtitle,
            appointment.date.toJavaLocalDate().format(DateTimeFormats.dateFormatter),
            appointment.timestamp.toJavaLocalDateTime().format(DateTimeFormats.timeFormatter),
            hostName
        )
        subtitleFlow.emit(string)
    }

    fun updateGameVote(gameVoteItem: GameVoteItem, value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (gameVoteItem.gameVote.value != value) {
                gameVoteItem.gameVote.value = value
                val success = backend.updateGameVote(gameVoteItem.gameVote)

                if (!success) {
                    gameVoteItem.gameVote.value = !value
                } else {
                    val gameVotes = mutableListOf<GameVoteItem>()
                    for (gameVote in gameSuggestionsFlow.value) {
                        if (gameVote.gameSuggestion.id == gameVoteItem.gameSuggestion.id) {
                            gameVotes.add(gameVoteItem)
                        } else {
                            gameVotes.add(gameVote)
                        }
                    }
                    gameSuggestionsFlow.emit(listOf())
                    delay(100)
                    gameSuggestionsFlow.emit(gameVotes)
                }
            }
        }
    }
}