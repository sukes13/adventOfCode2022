package be.fgov.sfpd.kata.aoc22.day23

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay23Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day23/exampleInput.txt")
         assertThat(part1(input)).isEqualTo(110)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day23/input.txt")
        assertThat(part1(input)).isEqualTo(4181)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day23/exampleInput.txt")
        assertThat(part2(input)).isEqualTo(20)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day23/input.txt")
        assertThat(part2(input)).isEqualTo(973)
    }

}

