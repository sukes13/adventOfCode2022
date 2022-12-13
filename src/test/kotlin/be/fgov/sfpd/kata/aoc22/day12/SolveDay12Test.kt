package be.fgov.sfpd.kata.aoc22.day12

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay12Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day12/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(31)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day12/input.txt")
        assertThat(part1(input)).isEqualTo(528)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day12/exampleInput.txt")
        assertThat(part2(input)).isEqualTo(29)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day12/input.txt")
        assertThat(part2(input)).isEqualTo(522)
    }

}