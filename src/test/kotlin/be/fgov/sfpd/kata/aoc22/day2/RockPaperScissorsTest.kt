package be.fgov.sfpd.kata.aoc22.day2

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day1.caloriesPerElf
import be.fgov.sfpd.kata.aoc22.day2.Shape.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RockPaperScissorsTest {

    @Test
    fun `example input line - parse to round`() {
        val input = "A Y"

        val actual = input.toRound()

        assertThat(actual).isEqualTo(Round(ROCK, PAPER))
    }

    @Test
    fun `example input - parse to list of rounds`() {
        val input = readFile("day2/exampleInput.txt")

        val actual = input.toRounds()

        assertThat(actual).containsExactly(Round(ROCK, PAPER), Round(PAPER, ROCK), Round(SCISSORS, SCISSORS))
    }

    @Test
    fun `Round(ROCK, PAPER) - get score - is 8`() {
        val input = Round(ROCK, PAPER)

        val actual = input.score()

        assertThat(actual).isEqualTo(8)
    }

    @Test
    fun `Round(PAPER, ROCK) - get score - is 1`() {
        val input = Round(PAPER, ROCK)

        val actual = input.score()

        assertThat(actual).isEqualTo(1)
    }

    @Test
    fun `example input line - to predicted roundd`() {
        val input = "A Y"

        val actual = input.toPredictedRound()

        assertThat(actual).isEqualTo(Round(ROCK, ROCK))
    }
    @ParameterizedTest(name = "Input:  \"{0}\" has result = \"{1}\"")
    @MethodSource("testRounds")
    fun `example input line - to predicted round - score`(input: String, result: Int) {
        val actual = input.toPredictedRound().score()

        assertThat(actual).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun testRounds() = listOf(
                Arguments.of("A Y", 4),
                Arguments.of("B X", 1),
                Arguments.of("C Z", 7),
        )
    }
}






