package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Player(
    val id: Long = -1,
    val name: String,
    val email: String,
    val location: String,
    @SerialName("favourite_food_id")
    val favouriteFoodId: Long
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Player {
            return Json.decodeFromString(input)
        }
    }
}
