package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Player(
    val id: Long,
    val name: String,
    val email: String,
    val location: String,
    val favouriteFoodId: Long
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Player {
            return Json.decodeFromString(input)
        }
    }
}
