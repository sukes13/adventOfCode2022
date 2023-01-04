package be.fgov.sfpd.kata.aoc22.day19

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay19Test {


    @Test
    fun `example input part 1`() {
        val input = readFile("day19/exampleInput.txt")
         assertThat(part1(input)).isEqualTo(33)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day19/input.txt")
        assertThat(part1(input)).isEqualTo(817)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day19/exampleInput.txt")
        val actual = input.toBlueprints().last().maxGeodes(32,22)

        assertThat(actual).isEqualTo(62)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day19/input.txt")
        assertThat(part2(input)).isEqualTo(4216)
    }

}

