package com.boardgamer.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class Message(
    val id: Long = -1,
    @SerialName("message_from_player_id")
    val playerId: Long,
    @SerialName("appointment_id")
    val appointmentId: Long,
    val timestamp: LocalDateTime,
    @SerialName("message_content")
    val message: String
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Message {
            return JsonSetup.json.decodeFromString(input)
        }
    }
}
