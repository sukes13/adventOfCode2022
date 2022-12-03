package be.fgov.sfpd.kata.aoc22.day3

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SolveDay3Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day3/exampleInput.txt")
        assertThat(solve1(input)).isEqualTo(157)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day3/input.txt")
        assertThat(solve1(input)).isEqualTo(7821)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day3/exampleInput.txt")
        assertThat(solve2(input)).isEqualTo(70)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day3/input.txt")
        assertThat(solve2(input)).isEqualTo(2752)
    }

    private fun solve1(input: String): Int = solution1(input)
    private fun solve2(input: String): Int = solution2(input)

}