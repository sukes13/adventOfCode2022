package be.fgov.sfpd.kata.aoc22.day11

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MonkeyInTheMiddleTest {
    @Test
    fun `test first round`() {
        val input = readFile("day11/exampleInput.txt")

        assertThat(input.toMonkeys().doRound().values.map { it.items }).containsExactly(
                listOf(20, 23, 27, 26),
                listOf(2080, 25, 167, 207, 401, 1046),
                listOf(),
                listOf()
        )
    }

    @Test
    fun `test two rounds`() {
        val input = readFile("day11/exampleInput.txt")

        assertThat(input.toMonkeys().doRound().doRound().values.map { it.items }).containsExactly(
                listOf(695, 10, 71, 135, 350),
                listOf(43, 49, 58, 55, 362),
                listOf(),
                listOf()
        )
    }

}