package com.boardgamer

import com.boardgamer.model.Appointment
import com.boardgamer.model.Evaluation
import com.boardgamer.model.Game
import com.boardgamer.model.GameSuggestion
import com.boardgamer.model.GameVote
import com.boardgamer.model.Message
import com.boardgamer.model.Player
import com.boardgamer.model.PlayerAppointment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class JsonConversionTests {
    @Test
    fun jsonConvertPlayer() {
        val expected = Player(5, "Check", "These", "values")
        val parsed = Player.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertAppointment() {
        val expected = Appointment(
            10,
            LocalDate(2025, 2, 5),
            LocalDateTime(2025, 5, 1, 10, 15, 5, 123),
            "heyThere",
            15
        )
        val parsed = Appointment.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertGameSuggestion() {
        val expected = GameSuggestion(123, 456, 789)
        val parsed = GameSuggestion.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertGame() {
        val expected = Game(11, "FunFunGame", "ThisIsASuperFunGameOnlyTheOnesWhoKnowKnow")
        val parsed = Game.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertEvaluation() {
        val expected = Evaluation(12, 45, 78, 5, 5, 5)
        val parsed = Evaluation.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertGameVote() {
        val expected = GameVote(12, 45, true)
        val parsed = GameVote.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertMessage() {
        val expected = Message(
            5,
            21,
            156,
            LocalDateTime(2025, 7, 13, 21, 14, 12, 123),
            "HeyGuysGoingToBeLate"
        )
        val parsed = Message.fromJson(expected.toJson())

        assert(parsed == expected)
    }

    @Test
    fun jsonConvertPlayerAppointment() {
        val expected = PlayerAppointment(12, 34)
        val parsed = PlayerAppointment.fromJson(expected.toJson())

        assert(parsed == expected)
    }
}