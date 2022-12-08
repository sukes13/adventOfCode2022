package be.fgov.sfpd.kata.aoc22.day8

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay8Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day8/exampleInput.txt")
        assertThat(solve1(input)).isEqualTo(21)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day8/input.txt")
        assertThat(solve1(input)).isEqualTo(1695)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day8/exampleInput.txt")
        assertThat(solve2(input)).isEqualTo(8)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day8/input.txt")
        assertThat(solve2(input)).isEqualTo(287040)
    }

    private fun solve1(input: String) = part1(input)
    private fun solve2(input: String) = part2(input)
}