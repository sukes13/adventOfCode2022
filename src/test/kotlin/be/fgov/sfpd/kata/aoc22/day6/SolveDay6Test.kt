package be.fgov.sfpd.kata.aoc22.day6

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay6Test {

    @Test
    fun `actual input part 1`() {
        val input = readFile("day6/input.txt")
        assertThat(solve1(input)).isEqualTo(1760)
    }
//
//    @Test
//    fun `example input part 2`() {
//        val input = readFile("day5/exampleInput.txt")
//        assertThat(solve2(input)).isEqualTo("MCD")
//    }
//
    @Test
    fun `actual input part 2`() {
        val input = readFile("day6/input.txt")
        assertThat(solve2(input)).isEqualTo(2974)
    }
//
    private fun solve1(input: String) = part1(input)
    private fun solve2(input: String) = part2(input)

}