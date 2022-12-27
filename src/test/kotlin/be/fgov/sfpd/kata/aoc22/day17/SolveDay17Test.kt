package be.fgov.sfpd.kata.aoc22.day17

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay17Test {


    @Test
    fun `example input part 1`() {
        val input = readFile("day17/exampleInput.txt")
         assertThat(part1(input)).isEqualTo(3068)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day17/input.txt")
        assertThat(part1(input)).isEqualTo(3147)
    }

//    @Test
//    fun `example input part 2`() {
//        val input = readFile("day17/exampleInput.txt")
//        assertThat(part2(input)).isEqualTo(1514285714288L)
//    }
//
//    @Test
//    fun `actual input part 2`() {
//        val input = readFile("day17/input.txt")
//        assertThat(part2(input)).isEqualTo(10457634860779L)
//    }

}

