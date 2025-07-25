package com.boardgamer.model

import kotlinx.serialization.json.Json

object JsonSetup {
    val json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
    }
}