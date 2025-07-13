package com.boardgamer

import com.boardgamer.api.BackendAPI
import com.boardgamer.model.RegistrationPlayer
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTest {
    val backend = BackendAPI()

    @Test
    fun authenticate() {
        val player = runBlocking { backend.authenticate("Anna Schmidt", "pass123") }
        assert(player.name == "Anna Schmidt") { "Expected player Anna Schmidt, but got: $player" }
    }

    @Test
    fun register() {
        //Note this test will fail whenever there already is a user with the e-mail in the db
        val player =
            RegistrationPlayer("Lucas", "example@mail.com", "secret", "BerlinCityBaby", 1)
        val id = runBlocking { backend.register(player) }
        assert(id > -1) { "Expected Id greater -1, but got: $id" }
    }

    @Test
    fun getAppointments() {
        val appointments = runBlocking { backend.getAppointments() }
        assert(appointments.size == 2) { "Expected two appointments got: $appointments" }
    }
}