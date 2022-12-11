package be.fgov.sfpd.kata.aoc22.day11

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay11Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day11/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(10605)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day11/input.txt")
        assertThat(part1(input)).isEqualTo(72884)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day11/exampleInput.txt")
        assertThat(part2(input)).isEqualTo(2713310158L)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day11/input.txt")
        assertThat(part2(input)).isEqualTo(15310845153L)
    }

}