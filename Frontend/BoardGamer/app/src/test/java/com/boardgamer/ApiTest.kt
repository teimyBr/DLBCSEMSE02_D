package com.boardgamer

import com.boardgamer.api.BackendAPI
import com.boardgamer.model.Appointment
import com.boardgamer.model.RegistrationPlayer
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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

    @Test
    fun addAppointment() {
        val appointment = Appointment(
            date = LocalDate(2025, 7, 13),
            timestamp = LocalDateTime(2025, 7, 13, 21, 11, 50, 123),
            location = "TheBestLocation",
            hostId = 1
        )
        val id = runBlocking { backend.insertAppointment(appointment) }
        assert(id > -1) { "Expected id > -1, but was $id" }
    }
}