package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PlayerAppointment(
    @SerialName("player_id")
    val playerId: Long,
    @SerialName("appointment_id")
    val appointmentId: Long
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): PlayerAppointment {
            return Json.decodeFromString(input)
        }
    }
}
