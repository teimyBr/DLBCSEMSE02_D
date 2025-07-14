package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FoodChoice(
    val id: Long,
    @SerialName("player_id")
    val playerId: Long,
    @SerialName("appointment_id")
    val appointmentId: Long,
    @SerialName("food_direction_id")
    val foodDirectionId: Long
) {
    companion object {
        fun fromJson(input: String): FoodChoice {
            return Json.decodeFromString(input)
        }
    }
}
