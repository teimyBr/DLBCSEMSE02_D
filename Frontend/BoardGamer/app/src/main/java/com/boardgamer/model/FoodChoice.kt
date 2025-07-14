package com.boardgamer.model

import kotlinx.serialization.json.Json

data class FoodChoice(
    val id: Long,
    val playerId: Long,
    val appointmentId: Long,
    val foodDirectionId: Long
) {
    companion object {
        fun fromJson(input: String): FoodChoice {
            return Json.decodeFromString(input)
        }
    }
}
