package com.boardgamer.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

@Serializable
@OptIn(ExperimentalTime::class)
data class Appointment(
    val id: Long = -1,
    val date: LocalDate,
    val timestamp: LocalDateTime,
    val location: String,
    @SerialName("host_id")
    val hostId: Long
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): Appointment {
            return Json.decodeFromString(input)
        }
    }
}
