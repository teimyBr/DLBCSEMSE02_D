package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class GameVote(
    val playerId: Long,
    val gameSuggestionId: Long,
    val value: Boolean
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): GameVote {
            return Json.decodeFromString(input)
        }
    }
}
