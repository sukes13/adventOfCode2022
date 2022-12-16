package be.fgov.sfpd.kata.aoc22.day13

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay13Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day13/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(13)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day13/input.txt")
        assertThat(part1(input)).isEqualTo(6101) //Too low
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day13/exampleInput.txt")
        assertThat(part2(input)).isEqualTo(140)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day13/input.txt")
        assertThat(part2(input)).isEqualTo(21909)
    }

}