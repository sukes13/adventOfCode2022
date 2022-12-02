package be.fgov.sfpd.kata.aoc22.day2

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day1.caloriesPerElf
import be.fgov.sfpd.kata.aoc22.day2.Shape.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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

}






