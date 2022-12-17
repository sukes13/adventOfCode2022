package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day14.Filler.ROCK
import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class RegolithReservoirTest {

    @ParameterizedTest(name = "Input:  \"{0}\" gives: \"{1}\"")
    @MethodSource("testRocks")
    fun `test toRockLine`(input: String, rockLine: List<CaveTile>) {
        Assertions.assertThat(input.toRockLine()).containsExactly(*rockLine.toTypedArray())
    }

    companion object {
        @JvmStatic
        fun testRocks() = listOf(
                Arguments.of("498,4 -> 498,6 -> 496,6", listOf(
                        CaveTile(point = Point(x = 498, y = 4), filler = ROCK),
                        CaveTile(point = Point(x = 498, y = 5), filler = ROCK),
                        CaveTile(point = Point(x = 498, y = 6), filler = ROCK),
                        CaveTile(point = Point(x = 497, y = 6), filler = ROCK),
                        CaveTile(point = Point(x = 496, y = 6), filler = ROCK)
                )),
        )
    }
}


