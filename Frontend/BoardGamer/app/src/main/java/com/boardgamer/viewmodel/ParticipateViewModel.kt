package com.boardgamer.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.R
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import com.boardgamer.model.Game
import com.boardgamer.model.GameSuggestion
import com.boardgamer.model.PlayerAppointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class GameSuggestionItem(
    val game: Game,
    private val selectedFlow: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val selected: StateFlow<Boolean> = selectedFlow.asStateFlow()
) {
    fun updateSelected() {
        selectedFlow.value = !selectedFlow.value
    }
}

class ParticipateViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val SCREEN_NAME = "Participate"
    }

    private val backend = BackendAPI()

    private val appointmentDetailsFlow = MutableStateFlow(
        AppointmentDetails(
            appointment = Appointment(
                -1,
                LocalDate(2025, 7, 25),
                LocalDateTime(2025, 7, 25, 7, 25, 45, 0),
                "FunTown",
                0
            ),
            ""
        )
    )
    val appointmentDetails = appointmentDetailsFlow.asStateFlow()

    private val gameListFlow = MutableStateFlow<List<GameSuggestionItem>>(listOf())
    val gameList = gameListFlow.asStateFlow()

    private var currentPlayerId: Long = -1L

    fun initialize(playerId: Long, appointmentId: Long) {
        this.currentPlayerId = playerId
        setAppointment(appointmentId)
    }

    fun setAppointment(appointmentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val appointment = backend.getAppointments().first { it.id == appointmentId }
            val hostName = backend.getPlayer(appointment.hostId)!!.name

            appointmentDetailsFlow.emit(AppointmentDetails(appointment, hostName))
            loadGameList(appointmentId)
        }
    }

    suspend fun loadGameList(appointmentId: Long) {
        val suggestions = backend.getGameSuggestions(appointmentId).map { it.gameId }
        val games = backend.getGames().filter { it.id !in suggestions }
        gameListFlow.emit(games.map { GameSuggestionItem(it) })
    }

    fun participate() {
        viewModelScope.launch(Dispatchers.IO) {
            val appointment = appointmentDetails.value.appointment
            val gameSuggestions = gameList.value.filter { it.selected.value }.map {
                GameSuggestion(
                    gameId = it.game.id,
                    appointmentId = appointment.id
                )
            }
            val successAddSuggestions = backend.addGameSuggestions(gameSuggestions)
            val successParticipate = backend.addPlayerAppointment(
                PlayerAppointment(
                    playerId = currentPlayerId,
                    appointmentId = appointment.id
                )
            )

            withContext(Dispatchers.Main) {
                val context = getApplication<Application>().applicationContext
                if (!successParticipate) {
                    Toast.makeText(
                        context,
                        R.string.participate_failure,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!successAddSuggestions) {
                    Toast.makeText(
                        context,
                        R.string.participate_partial_success,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        R.string.participate_success,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}