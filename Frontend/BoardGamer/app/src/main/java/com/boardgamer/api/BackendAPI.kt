package com.boardgamer.api

import android.util.Log
import com.boardgamer.model.Appointment
import com.boardgamer.model.Evaluation
import com.boardgamer.model.FoodChoice
import com.boardgamer.model.FoodDirection
import com.boardgamer.model.Game
import com.boardgamer.model.GameSuggestion
import com.boardgamer.model.GameVote
import com.boardgamer.model.Message
import com.boardgamer.model.Player
import com.boardgamer.model.PlayerAppointment
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
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.net.ConnectException

//For more info about the basicAddress see: https://developer.android.com/studio/run/emulator-networking
class BackendAPI(
    val basicAddress: String = "http://10.0.2.2:8000"
) {
    val TAG = "BackendAPI"
    val client = HttpClient(CIO)

    suspend fun authenticate(name: String, password: String): Player? {
        try {
            val response =
                client.post("$basicAddress/authenticate/${name.replace(" ", "%20")}/$password")
            if (response.status == HttpStatusCode.OK) {
                return Player.fromJson(response.bodyAsText())
            } else {
                Log.i(
                    TAG,
                    "Something went wrong when trying to authenticate, ${response.status}, ${response.bodyAsText()}"
                )
                return null
            }
        } catch (e: ConnectException) {
            Log.i(TAG, "Could not connect to backend", e)
            return null
        }
    }

    suspend fun register(player: RegistrationPlayer): Long {
        try {
            val response = post("$basicAddress/register/", player.toJson())
            if (response.status == HttpStatusCode.OK) {
                return getIdFromResponse(response)
            } else {
                Log.i(
                    TAG,
                    "Failed to register player, ${response.status}, ${response.bodyAsText()}"
                )
                return -1
            }
        } catch (e: ConnectException) {
            Log.i(TAG, "Could not connect to backend", e)
            return -1
        }
    }

    suspend fun getPlayer(playerId: Long): Player? {
        val response = client.get("$basicAddress/player/$playerId")
        if (response.status == HttpStatusCode.OK) {
            return Player.fromJson(response.bodyAsText())
        } else {
            Log.i(
                TAG,
                "Could not retrieve player with id: $playerId, ${response.status}, ${response.bodyAsText()}"
            )
            return null
        }
    }

    suspend fun getPlayers(): List<Player> {
        val response = client.get("$basicAddress/players")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(
                TAG, "Could not retrieve players, ${response.status}, ${response.bodyAsText()}"
            )
            return listOf()
        }
    }

    suspend fun isNextHost(playerId: Long): Boolean {
        val response = client.get("$basicAddress/isNextHost/$playerId")
        if (response.status == HttpStatusCode.OK) {
            return response.bodyAsText().toBoolean()
        } else {
            Log.i(
                TAG,
                "Failed to check if player is host: $playerId, ${response.status}, ${response.bodyAsText()}"
            )
            return false
        }
    }

    suspend fun getAppointments(): List<Appointment> {
        val response = client.get("$basicAddress/appointments")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(
                TAG,
                "Could not retrieve appointments, ${response.status}, ${response.bodyAsText()}"
            )
            return listOf()
        }
    }

    suspend fun addAppointment(appointment: Appointment): Long {
        val response = post("$basicAddress/appointments/insert/", appointment.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getIdFromResponse(response)
        } else {
            Log.i(TAG, "Failed to add appointment, ${response.status}, ${response.bodyAsText()}")
            return -1
        }
    }

    suspend fun getPlayerAppointments(): List<PlayerAppointment> {
        val response = client.get("$basicAddress/playerAppointments")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(
                TAG,
                "Failed to retrieve player appointment assignments, ${response.status}, ${response.bodyAsText()}"
            )
            return listOf()
        }
    }

    suspend fun addPlayerAppointment(playerAppointment: PlayerAppointment): Boolean {
        val response = post("$basicAddress/playerAppointment/insert/", playerAppointment.toJson())
        if (response.status == HttpStatusCode.OK) {
            return PlayerAppointment.fromJson(response.bodyAsText()) == playerAppointment
        } else {
            Log.i(
                TAG,
                "Failed to add player appointment link, ${response.status}, ${response.bodyAsText()}"
            )
            return false
        }
    }

    suspend fun updateAppointment(appointment: Appointment): Boolean {
        val response = post("$basicAddress/appointments/update/", appointment.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getBooleanFromResponse(response)
        } else {
            Log.i(TAG, "Failed to update appointment, ${response.status}, ${response.bodyAsText()}")
            return false
        }
    }

    suspend fun getGameSuggestions(appointmentId: Long): List<GameSuggestion> {
        val response = client.get("$basicAddress/gameSuggestions/$appointmentId")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(
                TAG,
                "Failed to get game suggestions, ${response.status}, ${response.bodyAsText()}"
            )
            return listOf()
        }
    }

    suspend fun addGameSuggestions(gameSuggestions: List<GameSuggestion>): Boolean {
        val response =
            post("$basicAddress/gameSuggestions/insert/", Json.encodeToString(gameSuggestions))
        if (response.status == HttpStatusCode.OK) {
            return getBooleanFromResponse(response)
        } else {
            Log.i(
                TAG,
                "Failed to add game suggestion, ${response.status}, ${response.bodyAsText()}"
            )
            return false
        }
    }

    suspend fun getGames(): List<Game> {
        val response = client.get("$basicAddress/games")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(TAG, "Failed to get Games, ${response.status}, ${response.bodyAsText()}")
            return listOf()
        }
    }

    suspend fun addGame(game: Game): Long {
        val response = post("$basicAddress/game/insert/", game.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getIdFromResponse(response)
        } else {
            Log.i(TAG, "Failed to add game, ${response.status}, ${response.bodyAsText()}")
            return -1
        }
    }

    suspend fun addGameVote(gameVote: GameVote): Long {
        val response = post("$basicAddress/gameVotes/insert/", gameVote.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getIdFromResponse(response)
        } else {
            Log.i(TAG, "Add Game Vote failed, ${response.status}, ${response.bodyAsText()}")
            return -1
        }
    }

    suspend fun updateGameVote(gameVote: GameVote): Boolean {
        val response = post("$basicAddress/gameVotes/update/", gameVote.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getBooleanFromResponse(response)
        } else {
            Log.i(TAG, "Updating Game Vote failed, ${response.status}, ${response.bodyAsText()}")
            return false
        }
    }

    suspend fun getGameVotesForPlayer(appointmentId: Long, playerId: Long): List<GameVote> {
        val response = client.get("$basicAddress/gameVotes/$appointmentId/$playerId")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(
                TAG,
                "couldn't get game votes for player, ${response.status}, ${response.bodyAsText()}"
            )
            return listOf()
        }
    }

    suspend fun getGameVotesForAppointment(appointmentId: Long): List<GameVote> {
        val response = client.get("$basicAddress/gameVotes/$appointmentId")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(
                TAG,
                "Couldn't get GameVotes for Appointment, ${response.status}, ${response.bodyAsText()}"
            )
            return listOf()
        }
    }

    suspend fun getFoodDirections(): List<FoodDirection> {
        val response = client.get("$basicAddress/foodDirections")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(TAG, "Couldn't get food directions, ${response.status}, ${response.bodyAsText()}")
            return listOf()
        }
    }

    suspend fun addFoodChoice(appointmentId: Long, playerId: Long, foodDirectionId: Long): Long {
        val response =
            client.post("$basicAddress/foodChoices/insert/$appointmentId/$playerId/$foodDirectionId")
        if (response.status == HttpStatusCode.OK) {
            return getIdFromResponse(response)
        } else {
            Log.i(TAG, "addFoodChoice failed, ${response.status}, ${response.bodyAsText()}")
            return -1
        }
    }

    suspend fun getFoodChoices(appointmentId: Long): List<FoodChoice> {
        val response = client.get("$basicAddress/foodChoices/$appointmentId")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(TAG, "Failed to getFoodChoices, ${response.status}, ${response.bodyAsText()}")
            return listOf()
        }
    }

    suspend fun getFoodChoice(appointmentId: Long, playerId: Long): FoodChoice? {
        val response = client.get("$basicAddress/foodChoices/$appointmentId/$playerId")
        if (response.status == HttpStatusCode.OK) {
            return FoodChoice.fromJson(response.bodyAsText())
        } else {
            Log.i(TAG, "Failed to get Food Choices, ${response.status}, ${response.bodyAsText()}")
            return null
        }
    }

    suspend fun addEvaluation(evaluation: Evaluation): Long {
        val response = post("$basicAddress/evaluations/insert/", evaluation.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getIdFromResponse(response)
        } else {
            Log.i(TAG, "Failed to add evaluation, ${response.status}, ${response.bodyAsText()}")
            return -1
        }
    }

    suspend fun getEvaluations(appointmentId: Long): List<Evaluation> {
        val response = client.get("$basicAddress/evaluations/$appointmentId")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(TAG, "Failed to get Evaluations, ${response.status}, ${response.bodyAsText()}")
            return listOf()
        }
    }

    suspend fun addMessage(message: Message): Long {
        val response = post("$basicAddress/messages/insert/", message.toJson())
        if (response.status == HttpStatusCode.OK) {
            return getIdFromResponse(response)
        } else {
            Log.i(TAG, "Failed to add Message, ${response.status}, ${response.bodyAsText()}")
            return -1
        }
    }

    suspend fun getMessages(appointmentId: Long): List<Message> {
        val response = client.get("$basicAddress/messages/$appointmentId")
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString(response.bodyAsText())
        } else {
            Log.i(TAG, "Failed to get Messages, ${response.status}, ${response.bodyAsText()}")
            return listOf()
        }
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