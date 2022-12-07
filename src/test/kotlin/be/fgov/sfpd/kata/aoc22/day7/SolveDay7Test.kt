package be.fgov.sfpd.kata.aoc22.day7

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay7Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day5/exampleInput.txt")
        assertThat(solve1(input)).isEqualTo("CMZ")
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day5/input.txt")
        assertThat(solve1(input)).isEqualTo("JDTMRWCQJ")
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

    private fun solve1(input: String) = be.fgov.sfpd.kata.aoc22.day5.part1(input)
    private fun solve2(input: String) = be.fgov.sfpd.kata.aoc22.day5.part2(input)
}