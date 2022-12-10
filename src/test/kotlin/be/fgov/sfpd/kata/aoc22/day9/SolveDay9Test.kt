package be.fgov.sfpd.kata.aoc22.day9

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay9Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day9/exampleInput.txt")
        assertThat(solve1(input)).isEqualTo(13)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day9/input.txt")
        assertThat(solve1(input)).isEqualTo(6256)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day9/exampleInputPart2.txt")
        assertThat(solve2(input)).isEqualTo(36)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day9/inputPart2.txt")
        assertThat(solve2(input)).isEqualTo(2665)
    }

    private fun solve1(input: String) = part1(input)
    private fun solve2(input: String) = part2(input)
}