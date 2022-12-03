package be.fgov.sfpd.kata.aoc22.day3

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RucksackTest {

    @ParameterizedTest(name = "Rucksack:  \"{0}\" has \"{1}\" in both compartments")
    @MethodSource("testRucksacks")
    fun `test rucksacks - find the shared item`(input: String, result: String) {
        val actual = input.splitCompartments().sharedItem()

        assertThat(actual).isEqualTo(result)
    }

    @Test
    fun `test input - find the shared item`(){
        val report: String = readFile("day3/exampleInput.txt")

        val groups = report.splitGroups()

        assertThat(groups.first().sharedItem()).isEqualTo("r")
        assertThat(groups.last().sharedItem()).isEqualTo("Z")
    }

    companion object {
        @JvmStatic
        fun testRucksacks() = listOf(
                Arguments.of("vJrwpWtwJgWrhcsFMMfFFhFp", "p"),
                Arguments.of("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", "L"),
                Arguments.of("PmmdzqPrVvPwwTWBwg", "P"),
                Arguments.of("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "v"),
                Arguments.of("ttgJtRGJQctTZtZT", "t"),
                Arguments.of("CrZsJsPPZsGzwwsLwLmpwMDw", "s"),
        )
    }

    @Test
    fun `get some scores - check value`() {
        assertThat(Priorities.priorityOf("a")).isEqualTo(1)
        assertThat(Priorities.priorityOf("p")).isEqualTo(16)
        assertThat(Priorities.priorityOf("L")).isEqualTo(38)
        assertThat(Priorities.priorityOf("P")).isEqualTo(42)
        assertThat(Priorities.priorityOf("t")).isEqualTo(20)
    }
}


