package be.fgov.sfpd.kata.aoc22.day2

import be.fgov.sfpd.kata.aoc22.day2.RPSResult.LOSS
import be.fgov.sfpd.kata.aoc22.day2.Shape.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RockPaperScissorsTest {

    @ParameterizedTest(name = "Input: \"{0}\" has score = \"{1}\"")
    @MethodSource("testRounds")
    fun `example input line - to round - score`(inputStr: String, result: Int) {
        val actual = inputStr.toRound().score

        assertThat(actual).isEqualTo(result)
    }

    @ParameterizedTest(name = "Predicted input:  \"{0}\" has score = \"{1}\"")
    @MethodSource("testPredictedRounds")
    fun `example input line - to predicted round - score`(input: String, result: Int) {
        val actual = input.toPredictedRound().score

        assertThat(actual).isEqualTo(result)
    }

    @Test
    fun `'A' - asShape- is ROCK`() {
        assertThat("A".asShape()).isEqualTo(ROCK)
    }

    @Test
    fun `'X' - asRPSResult- is LOSS`() {
        assertThat("X".asRPSResult()).isEqualTo(LOSS)
    }

    @Test
    fun `splitOnSpace('A Y')`() {
        assertThat("A Y".splitOnSpace()).isEqualTo("A" to "Y")
    }

    @Test
    fun `splitOnSpace('           A Y')`() {
        assertThat("         A Y ".splitOnSpace()).isEqualTo("A" to "Y")
    }

    companion object {
        @JvmStatic
        fun testRounds() = listOf(
                Arguments.of("A Y", 8),
                Arguments.of("B X", 1),
                Arguments.of("C Z", 6),
        )

        @JvmStatic
        fun testPredictedRounds() = listOf(
                Arguments.of("A Y", 4),
                Arguments.of("B X", 1),
                Arguments.of("C Z", 7),
        )
    }

}








