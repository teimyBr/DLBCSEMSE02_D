package com.boardgamer.api

import com.boardgamer.model.Appointment
import com.boardgamer.model.Evaluation
import com.boardgamer.model.FoodChoice
import com.boardgamer.model.FoodDirection
import com.boardgamer.model.Game
import com.boardgamer.model.GameSuggestion
import com.boardgamer.model.GameVote
import com.boardgamer.model.Message
import com.boardgamer.model.Player
import com.boardgamer.model.RegistrationPlayer
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
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
        val response = post("$basicAddress/register/", player.toJson())
        return getIdFromResponse(response)
    }

    suspend fun getAppointments(): List<Appointment> {
        return Json.decodeFromString(client.get("$basicAddress/appointments").bodyAsText())
    }

    suspend fun addAppointment(appointment: Appointment): Long {
        val response = post("$basicAddress/appointments/insert/", appointment.toJson())
        return getIdFromResponse(response)
    }

    suspend fun updateAppointment(appointment: Appointment): Boolean {
        val response = post("$basicAddress/appointments/update/", appointment.toJson())
        return getBooleanFromResponse(response)
    }

    suspend fun getGameSuggestions(appointmentId: Long): List<GameSuggestion> {
        return Json.decodeFromString(
            client.get("$basicAddress/gameSuggestions/$appointmentId").bodyAsText()
        )
    }

    suspend fun addGameSuggestions(gameSuggestions: List<GameSuggestion>): Boolean {
        val response =
            post("$basicAddress/gameSuggestions/insert/", Json.encodeToString(gameSuggestions))
        return getBooleanFromResponse(response)
    }

    suspend fun getGames(): List<Game> {
        return Json.decodeFromString(client.get("$basicAddress/games").bodyAsText())
    }

    suspend fun addGameVote(gameVote: GameVote): Long {
        val response = post("$basicAddress/gameVotes/insert/", gameVote.toJson())
        return getIdFromResponse(response)
    }

    suspend fun updateGameVote(gameVote: GameVote): Boolean {
        val response = post("$basicAddress/gameVotes/update/", gameVote.toJson())
        return getBooleanFromResponse(response)
    }

    suspend fun getGameVotesForPlayer(appointmentId: Long, playerId: Long): List<GameVote> {
        return Json.decodeFromString(
            client.get("$basicAddress/gameVotes/$appointmentId/$playerId").bodyAsText()
        )
    }

    suspend fun getGameVotesForAppointment(appointmentId: Long): List<GameVote> {
        return Json.decodeFromString(
            client.get("$basicAddress/gameVotes/$appointmentId").bodyAsText()
        )
    }

    suspend fun getFoodDirections(): List<FoodDirection> {
        return Json.decodeFromString(client.get("$basicAddress/foodDirections").bodyAsText())
    }

    suspend fun addFoodChoice(appointmentId: Long, playerId: Long, foodDirectionId: Long): Long {
        val response =
            client.post("$basicAddress/foodChoices/insert/$appointmentId/$playerId/$foodDirectionId")
        return getIdFromResponse(response)
    }

    suspend fun getFoodChoices(appointmentId: Long): List<FoodChoice> {
        return Json.decodeFromString(
            client.get("$basicAddress/foodChoices/$appointmentId").bodyAsText()
        )
    }

    suspend fun getFoodChoice(appointmentId: Long, playerId: Long): FoodChoice {
        return Json.decodeFromString(
            client.get("$basicAddress/foodChoices/$appointmentId/$playerId").bodyAsText()
        )
    }

    suspend fun addEvaluation(evaluation: Evaluation): Long {
        val response = post("$basicAddress/evaluations/insert/", evaluation.toJson())
        return getIdFromResponse(response)
    }

    suspend fun getEvaluations(appointmentId: Long): List<Evaluation> {
        return Json.decodeFromString(
            client.get("$basicAddress/evaluations/$appointmentId").bodyAsText()
        )
    }

    suspend fun addMessage(message: Message): Long {
        val response = post("$basicAddress/messages/insert/", message.toJson())
        return getIdFromResponse(response)
    }

    suspend fun getMessages(appointmentId: Long): List<Message> {
        return Json.decodeFromString(
            client.get("$basicAddress/messages/$appointmentId").bodyAsText()
        )
    }

    private suspend fun getIdFromResponse(response: HttpResponse): Long {
        val jsonObject = Json.decodeFromString<JsonObject>(response.bodyAsText())
        return jsonObject["id"]?.jsonPrimitive?.long ?: -1
    }

    private suspend fun getBooleanFromResponse(response: HttpResponse): Boolean {
        val jsonObject = Json.decodeFromString<JsonObject>(response.bodyAsText())
        return jsonObject["success"]?.jsonPrimitive?.boolean ?: false
    }

    private suspend fun post(url: String, json: String): HttpResponse {
        return client.post(url) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(json)
        }
    }
}