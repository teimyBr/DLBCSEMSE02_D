package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class GameSuggestion(
    val id: Long,
    val gameId: Long,
    val appointmentId: Long
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): GameSuggestion {
            return Json.decodeFromString(input)
        }
    }
}
