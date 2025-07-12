package com.boardgamer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FoodDirection(
    val id: Long,
    val designation: String
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun fromJson(input: String): FoodDirection {
            return Json.decodeFromString(input)
        }
    }
}
