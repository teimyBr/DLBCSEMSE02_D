package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class Message constructor(
    val id: Long,
    val playerId: Long,
    val appointmentId: Long,
    val timestamp: Instant,
    val message: String
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Message {
            return Json.decodeFromString(input)
        }
    }
}
