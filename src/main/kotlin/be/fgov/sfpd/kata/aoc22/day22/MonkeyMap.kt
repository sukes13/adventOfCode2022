package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.MoveCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand
import be.fgov.sfpd.kata.aoc22.day22.FacingDirection.*
import be.fgov.sfpd.kata.aoc22.day22.TileType.EMPTY
import be.fgov.sfpd.kata.aoc22.day22.TileType.WALL
import be.fgov.sfpd.kata.aoc22.day22.TurnDirection.LEFT
import be.fgov.sfpd.kata.aoc22.day22.TurnDirection.RIGHT
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String) = input.toBoard().tracePath().password

fun part2(input: String) = 0


class Board(val tiles: List<BoardTile>, val commands: List<BoardCommand>) {
    private val explorer = Explorer(tiles.filter { it.point.y == 1 }.minBy { it.point.x }.point, EAST)

    fun tracePath(): Explorer {
        return commands.fold(explorer) { explorer: Explorer, command ->
            explorer.execute(command, tiles)
        }
    }
}

data class Explorer(val position: Point, val facing: FacingDirection) {
    val password: Long = 1000L * position.y + 4 * position.x + facing.score

    fun execute(command: BoardCommand, tiles: List<BoardTile>): Explorer {
        return when (command) {
            is TurnCommand -> turn(command.direction)
            is MoveCommand -> move(command.steps, tiles)
        }
    }

    fun turn(direction: TurnDirection) = when (direction) {
        RIGHT -> copy(facing = facing.turnRight())
        LEFT -> copy(facing = facing.turnLeft())
    }

    private fun move(steps: Int, tiles: List<BoardTile>): Explorer {
        val line = when (facing) {
            NORTH  -> tiles.filter { it.point.x == position.x }
            EAST -> tiles.filter { it.point.y == position.y }
            SOUTH -> tiles.filter { it.point.x == position.x }
            WEST -> tiles.filter { it.point.y == position.y }
        }

        return (1..steps).fold(this) { explorer, _ ->
            explorer.moveStep(line).let { (explorer, stuck) ->
                if (stuck) return explorer
                explorer
            }
        }
    }

    private fun moveStep(line: List<BoardTile>): Pair<Explorer, Boolean> {
        val nextTile = when (facing) {
            NORTH -> line.singleOrNull { it.point.y == position.y - 1 } ?: line.maxBy { it.point.y }
            EAST -> line.singleOrNull { it.point.x == position.x + 1 } ?: line.minBy { it.point.x }
            SOUTH -> line.singleOrNull { it.point.y == position.y + 1 } ?: line.minBy { it.point.y }
            WEST -> line.singleOrNull { it.point.x == position.x - 1 } ?: line.maxBy { it.point.x }
        }
        return if (nextTile.type == EMPTY) {
            copy(position = nextTile.point) to false
        } else {
            this to true
        }
    }


}


enum class TileType { WALL, EMPTY }

enum class TurnDirection { LEFT, RIGHT }

enum class FacingDirection(val score: Int) {
    NORTH(3), EAST(0), SOUTH(1), WEST(2);

    fun turnRight() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun turnLeft() = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }
}

sealed class BoardCommand() {
    data class TurnCommand(val direction: TurnDirection) : BoardCommand()
    data class MoveCommand(val steps: Int) : BoardCommand()
}

data class BoardTile(val point: Point, val type: TileType)

internal fun String.toBoard(): Board {
    val (tiles, command) = splitOnEmptyLine()

    return Board(
            tiles = tiles.lines().flatMapIndexed { y, line ->
                line.mapIndexed { x, char ->
                    when (char) {
                        '.' -> BoardTile(Point(x + 1, y + 1), EMPTY)
                        '#' -> BoardTile(Point(x + 1, y + 1), WALL)
                        else -> null
                    }
                }
            }.filterNotNull(),
            commands = command.replace("R", ",R,").replace("L", ",L,").split(",").map {
                when (it) {
                    "R" -> TurnCommand(RIGHT)
                    "L" -> TurnCommand(LEFT)
                    else -> MoveCommand(it.toInt())
                }
            }
    )
}