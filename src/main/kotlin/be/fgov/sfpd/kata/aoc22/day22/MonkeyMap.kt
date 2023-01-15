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

fun part1(input: String, sideSize: Int) = input.toBoard(sideSize).tracePath(1).password

var cubeSidesChanges: List<CubeSideChange>? = null
fun part2(input: String, inputType: Pair<Int,List<CubeSideChange>>): Long {
    cubeSidesChanges = inputType.second
    return input.toBoard(inputType.first).tracePath(2).password
}


data class Board(val tiles: List<BoardTile>, val commands: List<BoardCommand>, val sideSize: Int) {
    private val explorer = Explorer(tiles.filter { it.point.y == 1 }.minBy { it.point.x }.point, EAST)

    fun tracePath(part: Int): Explorer =
            commands.fold(explorer) { explorer: Explorer, command ->
                when (part) {
                    1 -> explorer.execute(command, this)
                    else -> explorer.executeOnCube(command, this)
                }
            }
}

data class Explorer(val position: Point, val facing: FacingDirection) {
    val password: Long = 1000L * position.y + 4 * position.x + facing.score

    fun execute(command: BoardCommand, board: Board) = when (command) {
        is TurnCommand -> turn(command.direction)
        is MoveCommand -> moveOnFlat(command.steps, board.tiles)
    }

    fun executeOnCube(command: BoardCommand, board: Board) = when (command) {
        is TurnCommand -> turn(command.direction)
        is MoveCommand -> moveOnCube(command.steps, board)
    }

    fun turn(direction: TurnDirection) = when (direction) {
        RIGHT -> copy(facing = facing.turnRight())
        LEFT -> copy(facing = facing.turnLeft())
    }

    private fun moveOnFlat(steps: Int, tiles: List<BoardTile>): Explorer {
        val line = tiles.findLineFor(facing, position)

        return (1..steps).fold(this) { explorer, _ ->
            val nextTile = when (explorer.facing) {
                NORTH -> line.singleOrNull { it.point.y == explorer.position.y - 1 } ?: line.maxBy { it.point.y }
                EAST -> line.singleOrNull { it.point.x == explorer.position.x + 1 } ?: line.minBy { it.point.x }
                SOUTH -> line.singleOrNull { it.point.y == explorer.position.y + 1 } ?: line.minBy { it.point.y }
                WEST -> line.singleOrNull { it.point.x == explorer.position.x - 1 } ?: line.maxBy { it.point.x }
            }
            when (nextTile.type) {
                EMPTY -> copy(position = nextTile.point, facing = facing)
                else -> return explorer
            }
        }
    }

    private fun moveOnCube(steps: Int, board: Board): Explorer {
        val currentSideNr = board.tiles.single { it.point == position }.sideNr
        val currentSide = board.tiles.filter { it.sideNr == currentSideNr }
        var currentFacing = facing
        var currentLine = currentSide.findLineFor(currentFacing, position)

        return (1..steps).fold(this) { explorer, _ ->
            var nextTile = currentLine.nextTileFor(currentFacing, explorer.position)

            if (nextTile == null) {
                val sideChange = cubeSidesChanges!!.single { it.sideNr == currentSideNr && it.oldDirection == currentFacing }
                val newSide = board.tiles.filter { it.sideNr == sideChange.newSideNr }
                val distFromEdge = currentSide.findDistanceToEdgeFor(currentFacing, explorer.position, sideChange.flip, board.sideSize)
                val newPosition = newSide.enteringPositionFor(sideChange.newDirection, distFromEdge)

                currentLine = newSide.findLineFor(sideChange.newDirection, newPosition)
                currentFacing = sideChange.newDirection
                nextTile = newSide.single { it.point == newPosition }
            }

            when (nextTile.type) {
                EMPTY -> explorer.copy(position = nextTile.point, facing = currentFacing)
                else -> return explorer
            }
        }
    }

    private fun List<BoardTile>.nextTileFor(currentFacing: FacingDirection, position: Point) =
            when (currentFacing) {
                NORTH -> singleOrNull { it.point.y == position.y - 1 }
                EAST -> singleOrNull { it.point.x == position.x + 1 }
                SOUTH -> singleOrNull { it.point.y == position.y + 1 }
                WEST -> singleOrNull { it.point.x == position.x - 1 }
            }

    private fun List<BoardTile>.enteringPositionFor(facing: FacingDirection, distFromEdge: Int) =
            when (facing) {
                NORTH -> Point(x = minBy { it.point.x }.point.x + distFromEdge, y = maxBy { it.point.y }.point.y)
                EAST -> Point(x = minBy { it.point.x }.point.x, y = minBy { it.point.y }.point.y + distFromEdge)
                SOUTH -> Point(x = minBy { it.point.x }.point.x + distFromEdge, y = minBy { it.point.y }.point.y)
                WEST -> Point(x = maxBy { it.point.x }.point.x, y = minBy { it.point.y }.point.y + distFromEdge)
            }

    private fun List<BoardTile>.findDistanceToEdgeFor(facing: FacingDirection, position: Point, needsToFlip: Boolean, sideSize: Int) =
            when {
                facing.isVertical() -> position.x - minBy { it.point.x }.point.x
                else -> position.y - minBy { it.point.y }.point.y
            }.let {
                if (needsToFlip) sideSize - it - 1 else it
            }

    private fun List<BoardTile>.findLineFor(facing: FacingDirection, position: Point): List<BoardTile> =
            if (facing.isVertical()) filter { it.point.x == position.x } else filter { it.point.y == position.y }

}

enum class TileType { WALL, EMPTY }

enum class TurnDirection { LEFT, RIGHT }

enum class FacingDirection(val score: Int) {
    NORTH(3), EAST(0), SOUTH(1), WEST(2);

    fun isVertical() = this == NORTH || this == SOUTH

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

data class CubeSideChange(val sideNr: Int, val newSideNr: Int, val oldDirection: FacingDirection, val newDirection: FacingDirection, val flip: Boolean)

sealed class BoardCommand {
    data class TurnCommand(val direction: TurnDirection) : BoardCommand()
    data class MoveCommand(val steps: Int) : BoardCommand()
}

data class BoardTile(val point: Point, val type: TileType, val sideNr: Int = 0)

internal fun String.toBoard(sideSize: Int): Board {
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
            }.filterNotNull().withCubeSides(sideSize),
            commands = command.replace("R", ",R,").replace("L", ",L,").split(",").map {
                when (it) {
                    "R" -> TurnCommand(RIGHT)
                    "L" -> TurnCommand(LEFT)
                    else -> MoveCommand(it.toInt())
                }
            },
            sideSize = sideSize
    )
}


private fun List<BoardTile>.withCubeSides(sideSize: Int): List<BoardTile> {
    val topLefts = sideSize.allPossibleTopLefts()
    var sideNr = 0

    return topLefts.mapNotNull { topLeft ->
        when {
            any { it.point == topLeft } -> {
                sideNr += 1
                tileOnSide(topLeft, sideSize).map {
                    it.copy(sideNr = sideNr)
                }
            }

            else -> null
        }
    }.flatten()
}

private fun Int.allPossibleTopLefts() =
        (0..4).flatMap { y ->
            (0..4).map { x ->
                Point(x * this + 1, y * this + 1)
            }
        }


private fun List<BoardTile>.tileOnSide(topLeft: Point, sideSize: Int) =
        filter { it.point.x in (topLeft.x until topLeft.x + sideSize) && it.point.y in (topLeft.y until topLeft.y + sideSize) }


