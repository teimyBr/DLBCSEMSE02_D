package com.boardgamer.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class Message constructor(
    val id: Long = -1,
    val playerId: Long,
    val appointmentId: Long,
    val timestamp: LocalDateTime,
    val message: String
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Message {
            return Json.decodeFromString(input)
        }
    }
}
