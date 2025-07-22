package com.boardgamer

import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import com.boardgamer.model.Evaluation
import com.boardgamer.model.Game
import com.boardgamer.model.GameSuggestion
import com.boardgamer.model.GameVote
import com.boardgamer.model.Message
import com.boardgamer.model.RegistrationPlayer
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class ApiTest {
    val backend = BackendAPI("http://localhost:8000")

    @Test
    fun authenticate() {
        val player = runBlocking { backend.authenticate("Anna Schmidt", "pass123") }
        assert(player?.name == "Anna Schmidt") { "Expected player Anna Schmidt, but got: $player" }
    }

    @Test
    fun register() {
        //Note this test will fail whenever there already is a user with the e-mail in the db
        val player = RegistrationPlayer("Lucas", "example@mail.com", "secret", "BerlinCityBaby", 1)
        val id = runBlocking { backend.register(player) }
        assert(id > -1) { "Expected Id greater -1, but got: $id" }
    }

    @Test
    fun getPlayer() {
        val player = runBlocking { backend.getPlayer(1) }
        assert(player != null) { "Expected to retrieve player" }
    }

    @Test
    fun getAppointments() {
        val appointments = runBlocking { backend.getAppointments() }
        assert(appointments.size == 2) { "Expected two appointments got: $appointments" }
    }

    @Test
    fun addAppointment() { // Internal Server Error
        val appointment = Appointment(
            date = LocalDate(2025, 7, 13),
            timestamp = LocalDateTime(2025, 7, 13, 21, 11, 50, 123),
            location = "TheBestLocation",
            hostId = 1
        )
        val id = runBlocking { backend.addAppointment(appointment) }
        assert(id > -1) { "Expected id > -1, but was $id" }
    }

    @Test
    fun updateAppointment() {
        val appointment = Appointment(
            1, LocalDate(2025, 7, 14), LocalDateTime(2025, 7, 14, 18, 18, 18, 18), "DasDorf", 1
        )
        val success = runBlocking { backend.updateAppointment(appointment) }
        assert(success) { "Expected update to be successful" }
    }

    @Test
    fun getGameSuggestions() {
        val suggestions = runBlocking { backend.getGameSuggestions(1) }
        assert(suggestions.size == 2) { "Expected two suggestions, but got: $suggestions" }
    }

    @Test
    fun addGameSuggestion() { // Internal Server Error
        val suggestion = listOf(
            GameSuggestion(-1, 2, 2), GameSuggestion(-1, 3, 1)
        )
        val success = runBlocking { backend.addGameSuggestions(suggestion) }

        assert(success) { "Expected successfull insert of game suggestions" }
    }

    @Test
    fun getGames() {
        val games = runBlocking { backend.getGames() }
        assert(games.size == 3) { "Expected 3 games, but got: $games" }
    }

    @Test
    fun addGame() {
        val id = runBlocking {
            backend.addGame(
                Game(
                    name = "AwesomeGame",
                    description = "This is an awesome game, everyone wants to play"
                )
            )
        }
        assert(id > -1) { "Expected to successfully add the game to the backend" }
    }

    @Test
    fun addGameVote() {
        val gameVote = GameVote(3, 2, true)
        val id = runBlocking { backend.addGameVote(gameVote) }
        assert(id > -1) { "Expected successful insert of game vote, but got back id: $id" }
    }

    @Test
    fun updateGameVote() {
        val gameVote = GameVote(1, 1, false)
        val success = runBlocking { backend.updateGameVote(gameVote) }

        assert(success) { "Expected successful update" }
    }

    @Test
    fun getGameVotesForPlayer() {
        val votes = runBlocking { backend.getGameVotesForPlayer(1, 1) }
        assert(votes.size == 2) { "Expected 1 vote but got: $votes" }
    }

    @Test
    fun getGameVotesForAppointment() {
        val votes = runBlocking { backend.getGameVotesForAppointment(1) }
        assert(votes.size == 6) { "Expected 5 votes but got: $votes" }
    }

    @Test
    fun getFoodDirections() {
        val directions = runBlocking { backend.getFoodDirections() }
        assert(directions.size == 3) { "Expected 3 directions, but got $directions" }
    }

    @Test
    fun addFoodChoice() { // Always fails with code 200 as it catches exceptions
        val id = runBlocking { backend.addFoodChoice(2, 1, 1) }
        assert(id > -1) { "Expected id to be greater -1" }
    }

    @Test
    fun getFoodChoices() {
        val foodChoices = runBlocking { backend.getFoodChoices(1) }
        assert(foodChoices.size == 3) { "Expected 3 choices, but got: $foodChoices" }
    }

    @Test
    fun getFoodChoice() {
        val foodChoice = runBlocking { backend.getFoodChoice(1, 1) }
        assert(foodChoice != null) { "Expected to get a food choice" }
    }

    @Test
    fun getEvaluation() {
        val evaluation = runBlocking { backend.getEvaluations(1) }
        assert(evaluation.size == 3) { "Expected to get 3 evaluation, but got $evaluation" }
    }

    @Test
    fun addEvaluation() { // Internal Server Error
        val evaluation = Evaluation(-1, 1, 2, 3, 4, 3)
        val id = runBlocking { backend.addEvaluation(evaluation) }
        assert(id > -1) { "Expected to get a id back" }
    }

    @Test
    fun getMessages() {
        val messages = runBlocking { backend.getMessages(1) }
        assert(messages.size == 2) { "Expected 2 messages but got $messages" }
    }

    @Test
    fun addMessage() { // Internal Server Error
        val message = Message(
            -1, 1, 2, LocalDateTime(2025, 7, 14, 18, 58, 18, 16), "Dies ist ein Message Suprise!!!"
        )
        val id = runBlocking { backend.addMessage(message) }
        assert(id > -1) { "Expected a set id" }
    }
}