package be.fgov.sfpd.kata.aoc22.day15

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay15Test {


    @Test
    fun `example input part 1`() {
        val input = readFile("day15/exampleInput.txt")
        val lineForExample = 10
        assertThat(part1(input,lineForExample)).isEqualTo(26)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day15/input.txt")
        val line = 2000000
        assertThat(part1(input, line)).isEqualTo(5508234)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day15/exampleInput.txt")
        val maxForExample = 20
        assertThat(part2(input, maxForExample)).isEqualTo(56000011)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day15/input.txt")
        val maxForPart2 = 4000000
        assertThat(part2(input, maxForPart2)).isEqualTo(10457634860779L)
    }

}

