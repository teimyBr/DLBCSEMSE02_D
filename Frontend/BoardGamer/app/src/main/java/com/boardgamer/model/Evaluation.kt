package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Evaluation(
    val id: Long,
    val playerId: Long,
    val appointmentId: Long,
    val mealEvaluation: Int,
    val hostEvaluation: Int,
    val overallEvaluation: Int
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Evaluation {
            return Json.decodeFromString(input)
        }
    }
}
