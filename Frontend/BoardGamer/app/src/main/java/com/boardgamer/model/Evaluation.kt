package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Evaluation(
    val id: Long = -1,
    @SerialName("player_id")
    val playerId: Long,
    @SerialName("appointment_id")
    val appointmentId: Long,
    @SerialName("meal_evaluation")
    val mealEvaluation: Int,
    @SerialName("host_evaluation")
    val hostEvaluation: Int,
    @SerialName("overall_evaluation")
    val overallEvaluation: Int
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Evaluation {
            return Json.decodeFromString(input)
        }
    }
}
