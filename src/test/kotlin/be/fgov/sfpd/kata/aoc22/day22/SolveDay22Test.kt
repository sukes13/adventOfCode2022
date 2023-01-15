package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SolveDay22Test {

    @Test
    fun `example input part 1`() {
        val input = readFile("day22/exampleInput.txt")
         assertThat(part1(input,4)).isEqualTo(6032)
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("day22/input.txt")
        assertThat(part1(input,50)).isEqualTo(164014)
    }

    @Test
    fun `example input part 2`() {
        val input = readFile("day22/exampleInput.txt")
        val inputType = 4 to cubeSidesChangesExample
        assertThat(part2(input,inputType)).isEqualTo(5031)
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("day22/input.txt")
        val inputType = 50 to cubeSidesChangesInput
        assertThat(part2(input,inputType)).isEqualTo(47525)
    }

}

