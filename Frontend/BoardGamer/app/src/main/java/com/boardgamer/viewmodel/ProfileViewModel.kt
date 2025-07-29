package com.boardgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boardgamer.api.BackendAPI
import com.boardgamer.model.DateTimeFormats
import com.boardgamer.model.Player
import com.boardgamer.ui.toJavaLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class ProfileUiState(
    val userName: String = "",
    val playerGroup: List<Player> = emptyList(),
    val nextHost: Player? = null,
    val loggedOut: Boolean = false,
    val lastEvent: LastEventDetails? = null
)

data class LastEventDetails(
    val date: String,
    val hostRating: Int,
    val foodRating: Int,
    val generalRating: Int
)

class ProfileViewModel() : ViewModel() {
    companion object {
        const val SCREEN_NAME = "Profile"
    }

    private val backend = BackendAPI()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun initialize(playerId: Long) {
        loadProfileData(playerId)
    }

    @OptIn(ExperimentalTime::class)
    private fun loadProfileData(playerId: Long) {
        viewModelScope.launch {
            val currentUser = backend.getPlayer(playerId)
            val allPlayers = backend.getPlayers().sortedBy { it.id }
            val appointments = backend.getAppointments()
            var lastEventDetails: LastEventDetails? = null
            var nextHost: Player? = null

            val lastHostedAppointment = appointments
                .filter { it.hostId == playerId }
                .maxByOrNull { it.date }

            if (lastHostedAppointment != null) {
                val evaluations = backend.getEvaluations(lastHostedAppointment.id)
                if (evaluations.isNotEmpty()) {
                    val hostRating = (evaluations.map { it.hostEvaluation }.average() * 20).toInt()
                    val foodRating = (evaluations.map { it.mealEvaluation }.average() * 20).toInt()
                    val generalRating =
                        (evaluations.map { it.overallEvaluation }.average() * 20).toInt()

                    val formattedDate = lastHostedAppointment.date.toJavaLocalDate()
                        .format(DateTimeFormats.dateFormatter)

                    lastEventDetails = LastEventDetails(
                        date = formattedDate,
                        hostRating = hostRating,
                        foodRating = foodRating,
                        generalRating = generalRating
                    )
                }
            }

            if (allPlayers.isNotEmpty()) {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val lastAppointment = appointments
                    .filter { it.date <= today }
                    .maxByOrNull { it.date }

                val lastHostId = lastAppointment?.hostId
                val lastHostIndex = allPlayers.indexOfFirst { it.id == lastHostId }

                val nextHostIndex =
                    if (lastHostIndex == -1 || lastHostIndex >= allPlayers.lastIndex) {
                        0
                    } else {
                        lastHostIndex + 1
                    }
                nextHost = allPlayers[nextHostIndex]
            }


            if (allPlayers.isNotEmpty()) {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val lastAppointment = appointments
                    .filter { it.date <= today }
                    .maxByOrNull { it.date }

                val lastHostId = lastAppointment?.hostId
                val lastHostIndex = allPlayers.indexOfFirst { it.id == lastHostId }

                val nextHostIndex =
                    if (lastHostIndex == -1 || lastHostIndex >= allPlayers.lastIndex) {
                        0
                    } else {
                        lastHostIndex + 1
                    }

                nextHost = allPlayers[nextHostIndex]

            }

            if (currentUser != null) {
                _uiState.update {
                    it.copy(
                        userName = currentUser.name,
                        playerGroup = allPlayers,
                        nextHost = nextHost,
                        lastEvent = lastEventDetails
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.update { it.copy(loggedOut = true) }
    }
}
