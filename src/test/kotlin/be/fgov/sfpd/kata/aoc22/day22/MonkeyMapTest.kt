package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand.TurnLeftCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand.TurnRightCommand
import be.fgov.sfpd.kata.aoc22.day22.FacingDirection.*
import be.fgov.sfpd.kata.aoc22.day22.TileType.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class MonkeyMapTest {
    @Test
    fun `parse input to board`() {
        val (board,commands) = readFile("day22/exampleInput.txt").parse(4)

        assertThat(board.tiles).hasSize(96)
        assertThat(commands).hasSize(13)
        assertThat(board.tiles).containsExactly(BoardTile(Point(x=9, y=1), EMPTY, 1),
                BoardTile(Point(x=10, y=1),EMPTY, 1),
                BoardTile(Point(x=11, y=1),EMPTY, 1),
                BoardTile(Point(x=12, y=1),WALL, 1),
                BoardTile(Point(x=9, y=2),EMPTY, 1),
                BoardTile(Point(x=10, y=2),WALL, 1),
                BoardTile(Point(x=11, y=2),EMPTY, 1),
                BoardTile(Point(x=12, y=2),EMPTY, 1),
                BoardTile(Point(x=9, y=3),WALL, 1),
                BoardTile(Point(x=10, y=3),EMPTY, 1),
                BoardTile(Point(x=11, y=3),EMPTY, 1),
                BoardTile(Point(x=12, y=3),EMPTY, 1),
                BoardTile(Point(x=9, y=4),EMPTY, 1),
                BoardTile(Point(x=10, y=4),EMPTY, 1),
                BoardTile(Point(x=11, y=4),EMPTY, 1),
                BoardTile(Point(x=12, y=4),EMPTY, 1),
                BoardTile(Point(x=1, y=5),EMPTY, 2),
                BoardTile(Point(x=2, y=5),EMPTY, 2),
                BoardTile(Point(x=3, y=5),EMPTY, 2),
                BoardTile(Point(x=4, y=5),WALL, 2),
                BoardTile(Point(x=1, y=6),EMPTY, 2),
                BoardTile(Point(x=2, y=6),EMPTY, 2),
                BoardTile(Point(x=3, y=6),EMPTY, 2),
                BoardTile(Point(x=4, y=6),EMPTY, 2),
                BoardTile(Point(x=1, y=7),EMPTY, 2),
                BoardTile(Point(x=2, y=7),EMPTY, 2),
                BoardTile(Point(x=3, y=7),WALL, 2),
                BoardTile(Point(x=4, y=7),EMPTY, 2),
                BoardTile(Point(x=1, y=8),EMPTY, 2),
                BoardTile(Point(x=2, y=8),EMPTY, 2),
                BoardTile(Point(x=3, y=8),EMPTY, 2),
                BoardTile(Point(x=4, y=8),EMPTY, 2),
                BoardTile(Point(x=5, y=5),EMPTY, 3),
                BoardTile(Point(x=6, y=5),EMPTY, 3),
                BoardTile(Point(x=7, y=5),EMPTY, 3),
                BoardTile(Point(x=8, y=5),EMPTY, 3),
                BoardTile(Point(x=5, y=6),EMPTY, 3),
                BoardTile(Point(x=6, y=6),EMPTY, 3),
                BoardTile(Point(x=7, y=6),EMPTY, 3),
                BoardTile(Point(x=8, y=6),EMPTY, 3),
                BoardTile(Point(x=5, y=7),EMPTY, 3),
                BoardTile(Point(x=6, y=7),EMPTY, 3),
                BoardTile(Point(x=7, y=7),EMPTY, 3),
                BoardTile(Point(x=8, y=7),WALL, 3),
                BoardTile(Point(x=5, y=8),EMPTY, 3),
                BoardTile(Point(x=6, y=8),EMPTY, 3),
                BoardTile(Point(x=7, y=8),EMPTY, 3),
                BoardTile(Point(x=8, y=8),EMPTY, 3),
                BoardTile(Point(x=9, y=5),EMPTY, 4),
                BoardTile(Point(x=10, y=5),EMPTY, 4),
                BoardTile(Point(x=11, y=5),EMPTY, 4),
                BoardTile(Point(x=12, y=5),WALL, 4),
                BoardTile(Point(x=9, y=6),WALL, 4),
                BoardTile(Point(x=10, y=6),EMPTY, 4),
                BoardTile(Point(x=11, y=6),EMPTY, 4),
                BoardTile(Point(x=12, y=6),EMPTY, 4),
                BoardTile(Point(x=9, y=7),EMPTY, 4),
                BoardTile(Point(x=10, y=7),EMPTY, 4),
                BoardTile(Point(x=11, y=7),EMPTY, 4),
                BoardTile(Point(x=12, y=7),EMPTY, 4),
                BoardTile(Point(x=9, y=8),EMPTY, 4),
                BoardTile(Point(x=10, y=8),EMPTY, 4),
                BoardTile(Point(x=11, y=8),WALL, 4),
                BoardTile(Point(x=12, y=8),EMPTY, 4),
                BoardTile(Point(x=9, y=9),EMPTY, 5),
                BoardTile(Point(x=10, y=9),EMPTY, 5),
                BoardTile(Point(x=11, y=9),EMPTY, 5),
                BoardTile(Point(x=12, y=9),WALL, 5),
                BoardTile(Point(x=9, y=10),EMPTY, 5),
                BoardTile(Point(x=10, y=10),EMPTY, 5),
                BoardTile(Point(x=11, y=10),EMPTY, 5),
                BoardTile(Point(x=12, y=10),EMPTY, 5),
                BoardTile(Point(x=9, y=11),EMPTY, 5),
                BoardTile(Point(x=10, y=11),WALL, 5),
                BoardTile(Point(x=11, y=11),EMPTY, 5),
                BoardTile(Point(x=12, y=11),EMPTY, 5),
                BoardTile(Point(x=9, y=12),EMPTY, 5),
                BoardTile(Point(x=10, y=12),EMPTY, 5),
                BoardTile(Point(x=11, y=12),EMPTY, 5),
                BoardTile(Point(x=12, y=12),EMPTY, 5),
                BoardTile(Point(x=13, y=9),EMPTY, 6),
                BoardTile(Point(x=14, y=9),EMPTY, 6),
                BoardTile(Point(x=15, y=9),EMPTY, 6),
                BoardTile(Point(x=16, y=9),EMPTY, 6),
                BoardTile(Point(x=13, y=10),EMPTY, 6),
                BoardTile(Point(x=14, y=10),WALL, 6),
                BoardTile(Point(x=15, y=10),EMPTY, 6),
                BoardTile(Point(x=16, y=10),EMPTY, 6),
                BoardTile(Point(x=13, y=11),EMPTY, 6),
                BoardTile(Point(x=14, y=11),EMPTY, 6),
                BoardTile(Point(x=15, y=11),EMPTY, 6),
                BoardTile(Point(x=16, y=11),EMPTY, 6),
                BoardTile(Point(x=13, y=12),EMPTY, 6),
                BoardTile(Point(x=14, y=12),EMPTY, 6),
                BoardTile(Point(x=15, y=12),WALL, 6),
                BoardTile(Point(x=16, y=12),EMPTY, 6))
    }

    @ParameterizedTest(name = "Explorer facing:  \"{0}\", now faces: \"{1}\"")
    @MethodSource("testTurning")
    fun `test turning explorer right`(turnCommand: TurnCommand, start: FacingDirection, result: FacingDirection) {
        assertThat(turnCommand.turnWhen(start)).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun testTurning() = listOf(
                Arguments.of(TurnRightCommand, NORTH, EAST),
                Arguments.of(TurnRightCommand, EAST, SOUTH),
                Arguments.of(TurnRightCommand, SOUTH, WEST),
                Arguments.of(TurnRightCommand, WEST, NORTH),
                Arguments.of(TurnLeftCommand, NORTH, WEST),
                Arguments.of(TurnLeftCommand, WEST, SOUTH),
                Arguments.of(TurnLeftCommand, SOUTH, EAST),
                Arguments.of(TurnLeftCommand, EAST, NORTH),
        )
    }
}
