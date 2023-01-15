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
fun part2(input: String, sideSize: Int, inputType: String): Long {
    cubeSidesChanges = if(inputType == "example") cubeSidesChangesExample else cubeSidesChangesInput
    return input.toBoard(sideSize).tracePath(2).password
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
            explorer.moveStep(line).let { (explorer, stuck) ->
                if (stuck) return explorer
                explorer
            }
        }
    }

    private fun moveOnCube(steps: Int, board: Board): Explorer {
        val currentSideNr = board.tiles.single { it.point == position }.sideNr
        val currentSide = board.tiles.filter { it.sideNr == currentSideNr }
        var currentFacing = facing
        var currentLine = currentSide.findLineFor(currentFacing, position)

        return (1..steps).fold(this) { explorer, _ ->
            var nextTile = when (currentFacing) {
                NORTH -> currentLine.singleOrNull { it.point.y == explorer.position.y - 1 }
                EAST -> currentLine.singleOrNull { it.point.x == explorer.position.x + 1 }
                SOUTH -> currentLine.singleOrNull { it.point.y == explorer.position.y + 1 }
                WEST -> currentLine.singleOrNull { it.point.x == explorer.position.x - 1 }
            }

            if (nextTile == null) {
                val sidesChange = cubeSidesChanges!!.single { it.sideNr == currentSideNr && it.oldDirection == currentFacing }
                val newSide = board.tiles.filter { it.sideNr == sidesChange.newSideNr }
                val distFromEdge = currentSide.findDistanceToEdgeFor(currentFacing, explorer.position, sidesChange.flip, board.sideSize)
                val newPosition = newSide.newPositionFor(sidesChange.newDirection, distFromEdge)

                currentLine = newSide.findLineFor(sidesChange.newDirection, newPosition)
                currentFacing = sidesChange.newDirection
                nextTile = newSide.single { it.point == newPosition }
            }

            (if (nextTile.type == EMPTY) {
                println("Moving to position ${nextTile}, facing: $currentFacing ")
                explorer.copy(position = nextTile.point, facing = currentFacing) to false
            } else {
                println("Blocked at ${nextTile}, facing: $currentFacing ")
                explorer to true
            }).let { (explorer, stuck) ->
                if (stuck) return explorer
                explorer
            }
        }
    }

    private fun List<BoardTile>.newPositionFor(facing: FacingDirection, distFromEdge: Int) =
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

data class CubeSideChange(val sideNr: Int, val newSideNr: Int, val oldDirection: FacingDirection, val newDirection: FacingDirection, val flip: Boolean)

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

sealed class BoardCommand() {
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


val cubeSidesChangesExample = listOf(
        CubeSideChange(1, 2, NORTH, SOUTH, true),
        CubeSideChange(1, 4, SOUTH, SOUTH, false),
        CubeSideChange(1, 6, EAST, WEST, true),
        CubeSideChange(1, 3, WEST, SOUTH, false),

        CubeSideChange(2, 1, NORTH, SOUTH, true),
        CubeSideChange(2, 5, SOUTH, NORTH, true),
        CubeSideChange(2, 6, WEST, NORTH, false),
        CubeSideChange(2, 3, EAST, EAST, false),

        CubeSideChange(3, 1, NORTH, EAST, false),
        CubeSideChange(3, 5, SOUTH, EAST, true),
        CubeSideChange(3, 2, WEST, WEST, false),
        CubeSideChange(3, 4, EAST, EAST, true),

        CubeSideChange(4, 1, NORTH, NORTH, false),
        CubeSideChange(4, 5, SOUTH, SOUTH, false),
        CubeSideChange(4, 3, WEST, WEST, false),
        CubeSideChange(4, 6, EAST, SOUTH, true),

        CubeSideChange(5, 4, NORTH, NORTH, false),
        CubeSideChange(5, 2, SOUTH, NORTH, true),
        CubeSideChange(5, 3, WEST, NORTH, true),
        CubeSideChange(5, 6, EAST, EAST, false),

        CubeSideChange(6, 4, NORTH, WEST, true),
        CubeSideChange(6, 2, SOUTH, EAST, true),
        CubeSideChange(6, 5, WEST, WEST, false),
        CubeSideChange(6, 1, EAST, WEST, false),
)

val cubeSidesChangesInput = listOf(
        CubeSideChange(1, 6, NORTH, EAST, false),
        CubeSideChange(1, 3, SOUTH, SOUTH, false),
        CubeSideChange(1, 2, EAST, EAST, false),
        CubeSideChange(1, 4, WEST, EAST, true),

        CubeSideChange(2, 6, NORTH, NORTH, false),
        CubeSideChange(2, 3, SOUTH, WEST, false),
        CubeSideChange(2, 1, WEST, WEST, false),
        CubeSideChange(2, 5, EAST, WEST, true),

        CubeSideChange(3, 1, NORTH, NORTH, false),
        CubeSideChange(3, 5, SOUTH, SOUTH, false),
        CubeSideChange(3, 4, WEST, SOUTH, false),
        CubeSideChange(3, 2, EAST, NORTH, false),

        CubeSideChange(4, 3, NORTH, EAST, false),
        CubeSideChange(4, 6, SOUTH, SOUTH, false),
        CubeSideChange(4, 1, WEST, EAST, true),
        CubeSideChange(4, 5, EAST, EAST, false),

        CubeSideChange(5, 3, NORTH, NORTH, false),
        CubeSideChange(5, 6, SOUTH, WEST, false),
        CubeSideChange(5, 4, WEST, WEST, false),
        CubeSideChange(5, 2, EAST, WEST, true),

        CubeSideChange(6, 4, NORTH, NORTH, false),
        CubeSideChange(6, 2, SOUTH, SOUTH, false),
        CubeSideChange(6, 1, WEST, SOUTH, false),
        CubeSideChange(6, 5, EAST, NORTH, false),
)