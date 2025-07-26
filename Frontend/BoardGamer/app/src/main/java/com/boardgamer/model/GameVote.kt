package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GameVote(
    @SerialName("player_id")
    val playerId: Long,
    @SerialName("game_suggestion_id")
    val gameSuggestionId: Long,
    @SerialName("vote_value")
    val value: Boolean
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): GameVote {
            return JsonSetup.json.decodeFromString(input)
        }
    }
}
