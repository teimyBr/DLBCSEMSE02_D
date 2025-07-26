package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameSuggestion(
    val id: Long = -1,
    @SerialName("game_id")
    val gameId: Long,
    @SerialName("appointment_id")
    val appointmentId: Long
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): GameSuggestion {
            return JsonSetup.json.decodeFromString(input)
        }
    }
}
