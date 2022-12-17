package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay14Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day14/exampleInput.txt")
        assertThat(part1(input)).isEqualTo(24)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day14/input.txt")
        assertThat(part1(input)).isEqualTo(715)
    }
//
//    @Test
//    fun `example input part 2`() {
//        val input = readFile("day14/exampleInput.txt")
//        assertThat(part2(input)).isEqualTo(140)
//    }
//
//    @Test
//    fun `actual input part 2`() {
//        val input = readFile("day14/input.txt")
//        assertThat(part2(input)).isEqualTo(21909)
//    }

}