package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FoodDirection(
    val id: Long = -1,
    val designation: String
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): FoodDirection {
            return Json.decodeFromString(input)
        }
    }
}
