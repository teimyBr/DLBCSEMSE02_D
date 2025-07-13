package com.boardgamer.api

import com.boardgamer.model.Appointment
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class BackendAPI {
    val client = HttpClient(CIO)

    suspend fun getAppointments(): List<Appointment> {
        return Json.decodeFromString(client.get("http://localhost:8000/appointments").bodyAsText())
    }
}