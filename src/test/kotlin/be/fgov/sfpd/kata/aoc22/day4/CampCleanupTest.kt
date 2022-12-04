package be.fgov.sfpd.kata.aoc22.day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CampCleanupTest {

    @ParameterizedTest(name = "Zones:  \"{0}\" have complete overlap at: \"{1}\"")
    @MethodSource("testCompleteOverlaps")
    fun `test input - count complete overlaps`(inputString: String, result: Int) {
        val input = part1(inputString)

        assertThat(input).isEqualTo(result)
    }

    @ParameterizedTest(name = "Zones:  \"{0}\" overlap at: \"{1}\"")
    @MethodSource("testZones")
    fun `test input - find overlap`(input: String, result: Set<Int>) {
        val actual = input.toSweeps().overlap()

        assertThat(actual).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun testCompleteOverlaps() = listOf(
                Arguments.of("2-4,6-8", 0),
                Arguments.of("2-3,4-5", 0),
                Arguments.of("5-7,7-9", 0),
                Arguments.of("2-8,3-7", 1),
                Arguments.of("6-6,4-6", 1),
                Arguments.of("2-6,4-8", 0),
        )

        @JvmStatic
        fun testZones() = listOf(
                Arguments.of("2-4,6-8", setOf<Int>()),
                Arguments.of("2-3,4-5", setOf<Int>()),
                Arguments.of("5-7,7-9", setOf(7)),
                Arguments.of("2-8,3-7", (3..7).toSet()),
                Arguments.of("6-6,4-6", setOf(6)),
                Arguments.of("2-6,4-8", (4..6).toSet()),
        )
    }

    @Test
    fun `test to sweeps`() {
        assertThat("2-4,6-8".toSweeps()).containsExactly((2 .. 4).toSet() , (6 .. 8).toSet())
    }
}



