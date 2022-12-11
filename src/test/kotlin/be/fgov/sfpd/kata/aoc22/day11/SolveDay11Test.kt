package be.fgov.sfpd.kata.aoc22.day11

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay11Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day11/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(13140)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day11/input.txt")
        assertThat(part1(input)).isEqualTo(12840)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day11/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(12840)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day11/input.txt")
        assertThat(part1(input)).isEqualTo(12840)
    }

}