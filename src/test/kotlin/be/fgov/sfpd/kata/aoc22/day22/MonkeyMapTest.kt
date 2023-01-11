package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day22.FacingDirection.*
import be.fgov.sfpd.kata.aoc22.day22.TurnDirection.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class MonkeyMapTest {
    @Test
    fun `parse input to board`() {
        val board = readFile("day22/exampleInput.txt").toBoard()

        assertThat(board.tiles).hasSize(96)
        assertThat(board.commands).hasSize(13)
    }

    @ParameterizedTest(name = "Explorer facing:  \"{0}\", now faces: \"{1}\"")
    @MethodSource("testTurning")
    fun `test turning explorer right`(turning : TurnDirection, start: FacingDirection, result: FacingDirection) {
            val explorer = Explorer(Point(0, 0), start).turn(turning)
            assertThat(explorer.facing).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun testTurning() = listOf(
                Arguments.of(RIGHT, NORTH, EAST),
                Arguments.of(RIGHT, EAST, SOUTH),
                Arguments.of(RIGHT, SOUTH, WEST),
                Arguments.of(RIGHT, WEST, NORTH),
                Arguments.of(LEFT, NORTH, WEST),
                Arguments.of(LEFT, WEST, SOUTH),
                Arguments.of(LEFT, SOUTH, EAST),
                Arguments.of(LEFT, EAST, NORTH),
        )
    }
}
