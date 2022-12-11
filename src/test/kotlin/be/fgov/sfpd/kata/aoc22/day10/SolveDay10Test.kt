package be.fgov.sfpd.kata.aoc22.day10

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay10Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day10/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(13140)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day10/input.txt")
        assertThat(part1(input)).isEqualTo(12840)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day10/exampleInput.txt")
        val result = readFile("day10/exampleInputResult.txt")
        assertThat(part2(input)).isEqualTo(result.trimIndent())
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day10/input.txt")
        val result = readFile("day10/inputResult.txt")
        assertThat(part2(input)).isEqualTo(result.trimIndent())
    }

}