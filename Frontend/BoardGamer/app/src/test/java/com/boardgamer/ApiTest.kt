package com.boardgamer

import com.boardgamer.api.BackendAPI
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTest {
    val backend = BackendAPI()

    @Test
    fun getAppointments() {
        val appointments = runBlocking { backend.getAppointments() }
        assert(appointments.size == 2) {"Expected two appointments got: $appointments"}
    }
}