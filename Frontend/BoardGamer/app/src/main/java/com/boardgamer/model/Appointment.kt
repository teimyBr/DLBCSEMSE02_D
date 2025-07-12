package com.boardgamer.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
@OptIn(ExperimentalTime::class)
data class Appointment(
    val id: Long,
    val date: Instant,
    val location: String,
    val hostId: Long
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Appointment {
            return Json.decodeFromString(input)
        }
    }
}
