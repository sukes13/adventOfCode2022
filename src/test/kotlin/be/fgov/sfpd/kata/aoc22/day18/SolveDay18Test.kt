package be.fgov.sfpd.kata.aoc22.day18

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay18Test {


    @Test
    fun `example input part 1`() {
        val input = readFile("day18/exampleInput.txt")
         assertThat(part1(input)).isEqualTo(64)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day18/input.txt")
        assertThat(part1(input)).isEqualTo(3346)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day18/exampleInput.txt")
        assertThat(part2(input)).isEqualTo(58)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day18/input.txt")
        assertThat(part2(input)).isEqualTo(1980)
    }

}

