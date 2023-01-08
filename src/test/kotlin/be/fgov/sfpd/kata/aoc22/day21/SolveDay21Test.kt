package be.fgov.sfpd.kata.aoc22.day21

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay21Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day21/exampleInput.txt")
         assertThat(part1(input)).isEqualTo(152)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day21/input.txt")
        assertThat(part1(input)).isEqualTo(256997859093114)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day21/exampleInput.txt")
        assertThat(part2(input)).isEqualTo(301)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day21/input.txt")
        assertThat(part2(input)).isEqualTo(3952288690726)
    }

}

