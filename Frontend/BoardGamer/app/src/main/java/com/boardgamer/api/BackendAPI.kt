package com.boardgamer.api

import com.boardgamer.model.Appointment
import com.boardgamer.model.Player
import com.boardgamer.model.RegistrationPlayer
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

class BackendAPI {
    val client = HttpClient(CIO)
    val basicAddress = "http://localhost:8000"

    suspend fun authenticate(name: String, password: String): Player {
        return Json.decodeFromString(
            client.post("$basicAddress/authenticate/${name.replace(" ", "%20")}/$password")
                .bodyAsText()
        )
    }

    suspend fun register(player: RegistrationPlayer): Long {
        val response = client.post("$basicAddress/register/") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(player.toJson())
        }
        val jsonObject = Json.decodeFromString<JsonObject>(response.bodyAsText())
        return jsonObject["id"]?.jsonPrimitive?.long ?: -1
    }

    suspend fun getAppointments(): List<Appointment> {
        return Json.decodeFromString(client.get("$basicAddress/appointments").bodyAsText())
    }
}