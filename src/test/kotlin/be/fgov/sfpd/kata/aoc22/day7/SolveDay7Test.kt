package be.fgov.sfpd.kata.aoc22.day7

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay7Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day7/exampleInput.txt")
        assertThat(solve1(input)).isEqualTo(95437)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day7/input.txt")
        assertThat(solve1(input)).isEqualTo(1447046)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day5/exampleInput.txt")
        assertThat(solve2(input)).isEqualTo("MCD")
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day5/input.txt")
        assertThat(solve2(input)).isEqualTo("VHJDDCWRD")
    }

    private fun solve1(input: String) = part1(input)
    private fun solve2(input: String) = be.fgov.sfpd.kata.aoc22.day5.part2(input)
}