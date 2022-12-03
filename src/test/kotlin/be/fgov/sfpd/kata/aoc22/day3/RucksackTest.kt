package be.fgov.sfpd.kata.aoc22.day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RucksackTest {

    @ParameterizedTest(name = "Rucksack:  \"{0}\" has \"{1}\" in both compartments")
    @MethodSource("testRucksacks")
    fun `test rucksacks - find the shared item`(input: String, result: String) {
        val actual = input.sharedItem()

        assertThat(actual).isEqualTo(result)
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
        assertThat(Priorities.scoreFor("a")).isEqualTo(1)
        assertThat(Priorities.scoreFor("p")).isEqualTo(16)
        assertThat(Priorities.scoreFor("L")).isEqualTo(38)
        assertThat(Priorities.scoreFor("P")).isEqualTo(42)
        assertThat(Priorities.scoreFor("t")).isEqualTo(20)
    }
}


