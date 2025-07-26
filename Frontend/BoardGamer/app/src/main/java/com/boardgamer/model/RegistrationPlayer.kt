package com.boardgamer.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationPlayer(
    val name: String,
    val email: String,
    val password: String,
    val location: String
) {
    fun toJson(): String {
        return JsonSetup.json.encodeToString(this)
    }
}
