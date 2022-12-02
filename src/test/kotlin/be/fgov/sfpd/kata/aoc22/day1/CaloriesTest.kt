package be.fgov.sfpd.kata.aoc22.day1

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CaloriesTest {

    @Test
    fun `example input - get sum of calories per elf`() {
        val report: String = readFile("day1/exampleInput.txt")

        val result = report.caloriesPerElf()

        assertThat(result).containsExactly(6000, 4000, 11000, 24000, 10000)
    }
}


