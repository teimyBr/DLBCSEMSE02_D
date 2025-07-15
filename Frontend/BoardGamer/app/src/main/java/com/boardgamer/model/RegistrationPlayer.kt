package com.boardgamer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegistrationPlayer(
    val name: String,
    val email: String,
    val password: String,
    val location: String,
    @SerialName("favourite_food_id")
    val favouriteFoodId: Long
) {
    fun toJson(): String {
        return Json.encodeToString(this)
    }
}
