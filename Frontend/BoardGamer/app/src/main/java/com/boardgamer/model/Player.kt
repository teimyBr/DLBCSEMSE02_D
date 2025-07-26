package com.boardgamer.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Long = -1,
    val name: String,
    val email: String,
    val location: String
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Player {
            return JsonSetup.json.decodeFromString(input)
        }
    }
}
