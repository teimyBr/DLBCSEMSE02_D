package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Game(
    val id: Long = -1,
    val name: String,
    val description: String
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Game {
            return Json.decodeFromString(input)
        }
    }
}
