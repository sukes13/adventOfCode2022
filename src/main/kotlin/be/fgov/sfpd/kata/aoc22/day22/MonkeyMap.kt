package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.MoveCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand.TurnLeftCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand.TurnRightCommand
import be.fgov.sfpd.kata.aoc22.day22.Explorer.CubeExplorer
import be.fgov.sfpd.kata.aoc22.day22.Explorer.FlatExplorer
import be.fgov.sfpd.kata.aoc22.day22.FacingDirection.*
import be.fgov.sfpd.kata.aoc22.day22.TileType.EMPTY
import be.fgov.sfpd.kata.aoc22.day22.TileType.WALL
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String, sideSize: Int): Long {
    val (board, commands) = input.parse(sideSize)

    return FlatExplorer(board.startPosition, EAST).tracePath(board, commands, listOf()).password
}

fun part2(input: String, inputType: Pair<Int, List<CubeSideChange>>): Long {
    val (board, commands) = input.parse(inputType.first)

    return CubeExplorer(board.startPosition, EAST).tracePath(board, commands, inputType.second).password
}


sealed class Explorer(position: Point, facing: FacingDirection) {
    abstract fun turn(command: TurnCommand): Explorer
    abstract fun move(steps: Int, board: Board, cubeSideChanges: List<CubeSideChange>): Explorer

    val password: Long = 1000L * position.y + 4 * position.x + facing.score


    fun tracePath(board: Board, commands: List<BoardCommand>, cubeSidesChanges: List<CubeSideChange>): Explorer =
            commands.fold(this) { explorer: Explorer, command ->
                when (command) {
                    is TurnCommand -> explorer.turn(command)
                    is MoveCommand -> explorer.move(command.steps, board, cubeSidesChanges)
                }
            }

    data class FlatExplorer(val position: Point, val facing: FacingDirection) : Explorer(position, facing) {
        override fun turn(command: TurnCommand) = copy(facing = command.turnWhen(facing))

        override fun move(steps: Int, board: Board, cubeSideChanges: List<CubeSideChange>): FlatExplorer {
            val line = board.tiles.findLineFor(facing, position)

            return (1..steps).fold(this) { explorer, _ ->
                val nextTile = line.nextTileFor(explorer.facing, explorer.position) ?: line.reEnterWhen(explorer.facing)

                when (nextTile.type) {
                    EMPTY -> copy(position = nextTile.point)
                    else -> return explorer
                }
            }
        }

        private fun List<BoardTile>.reEnterWhen(facing: FacingDirection) = when (facing) {
            NORTH -> maxBy { it.point.y }
            EAST -> minBy { it.point.x }
            SOUTH -> minBy { it.point.y }
            WEST -> maxBy { it.point.x }
        }
    }

    data class CubeExplorer(val position: Point, val facing: FacingDirection) : Explorer(position, facing) {
        override fun turn(command: TurnCommand) = copy(facing = command.turnWhen(facing))

        override fun move(steps: Int, board: Board, cubeSideChanges: List<CubeSideChange>): CubeExplorer {
            val currentSideNr = board.tiles.single { it.point == position }.sideNr
            val currentSide = board.tiles.filter { it.sideNr == currentSideNr }
            var currentFacing = facing
            var currentLine = currentSide.findLineFor(currentFacing, position)

            return (1..steps).fold(this) { explorer, _ ->
                var nextTile = currentLine.nextTileFor(explorer.facing, explorer.position)

                if (nextTile == null) {
                    val sideChange = cubeSideChanges.single { it.sideNr == currentSideNr && it.oldDirection == currentFacing }
                    val newSide = board.tiles.filter { it.sideNr == sideChange.newSideNr }
                    val distFromEdge = currentSide.distanceToEdgeFor(currentFacing, explorer.position, sideChange.flip, board.sideSize)
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

        private fun List<BoardTile>.distanceToEdgeFor(facing: FacingDirection, position: Point, needsToFlip: Boolean, sideSize: Int) =
                when {
                    facing.isVertical() -> position.x - minBy { it.point.x }.point.x
                    else -> position.y - minBy { it.point.y }.point.y
                }.let {
                    if (needsToFlip) sideSize - it - 1 else it
                }

        private fun List<BoardTile>.enteringPositionFor(facing: FacingDirection, distFromEdge: Int) = when (facing) {
            NORTH -> Point(x = minBy { it.point.x }.point.x + distFromEdge, y = maxBy { it.point.y }.point.y)
            EAST -> Point(x = minBy { it.point.x }.point.x, y = minBy { it.point.y }.point.y + distFromEdge)
            SOUTH -> Point(x = minBy { it.point.x }.point.x + distFromEdge, y = minBy { it.point.y }.point.y)
            WEST -> Point(x = maxBy { it.point.x }.point.x, y = minBy { it.point.y }.point.y + distFromEdge)
        }
    }

    internal fun List<BoardTile>.findLineFor(facing: FacingDirection, position: Point): List<BoardTile> =
            if (facing.isVertical()) filter { it.point.x == position.x } else filter { it.point.y == position.y }

    internal fun List<BoardTile>.nextTileFor(facing: FacingDirection, position: Point) = when (facing) {
        NORTH -> singleOrNull { it.point.y == position.y - 1 }
        EAST -> singleOrNull { it.point.x == position.x + 1 }
        SOUTH -> singleOrNull { it.point.y == position.y + 1 }
        WEST -> singleOrNull { it.point.x == position.x - 1 }
    }
}


data class Board(val tiles: List<BoardTile>, val sideSize: Int) {
    val startPosition = tiles.filter { it.point.y == 1 }.minBy { it.point.x }.point
}

sealed class BoardCommand {

    data class MoveCommand(val steps: Int) : BoardCommand()

    sealed class TurnCommand : BoardCommand() {
        abstract fun turnWhen(facing: FacingDirection): FacingDirection

        object TurnLeftCommand : TurnCommand() {
            override fun turnWhen(facing: FacingDirection) = when (facing) {
                NORTH -> WEST
                WEST -> SOUTH
                SOUTH -> EAST
                EAST -> NORTH
            }
        }

        object TurnRightCommand : TurnCommand() {
            override fun turnWhen(facing: FacingDirection) = when (facing) {
                NORTH -> EAST
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
            }
        }
    }
}

data class BoardTile(val point: Point, val type: TileType, val sideNr: Int = 0)

data class CubeSideChange(val sideNr: Int, val newSideNr: Int, val oldDirection: FacingDirection, val newDirection: FacingDirection, val flip: Boolean)

enum class TileType { WALL, EMPTY }

enum class FacingDirection(val score: Int) {
    NORTH(3), EAST(0), SOUTH(1), WEST(2);

    fun isVertical() = this == NORTH || this == SOUTH
}


//Parsing...
internal fun String.parse(sideSize: Int): Pair<Board, List<BoardCommand>> {
    val (tiles, command) = splitOnEmptyLine()

    return Board(
            tiles = tiles.toUnsidedTiles().addCubeSides(sideSize),
            sideSize = sideSize
    ) to command.replace("R", ",R,").replace("L", ",L,").split(",").map {
        when (it) {
            "R" -> TurnRightCommand
            "L" -> TurnLeftCommand
            else -> MoveCommand(it.toInt())
        }
    }
}

private fun String.toUnsidedTiles() = lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, char ->
        when (char) {
            '.' -> BoardTile(Point(x + 1, y + 1), EMPTY)
            '#' -> BoardTile(Point(x + 1, y + 1), WALL)
            else -> null
        }
    }
}.filterNotNull()

private fun List<BoardTile>.addCubeSides(sideSize: Int): List<BoardTile> {
    val possibleTopLefts = (0..4).flatMap { y ->
        (0..4).map { x ->
            Point(x * sideSize + 1, y * sideSize + 1)
        }
    }
    var sideNr = 1

    return possibleTopLefts.mapNotNull { topLeft ->
        val tilesOnSide = filter { it.point.x in (topLeft.x until topLeft.x + sideSize) && it.point.y in (topLeft.y until topLeft.y + sideSize) }
        if (tilesOnSide.isNotEmpty())
            tilesOnSide.map {
                it.copy(sideNr = sideNr)
            }.also { sideNr += 1 }
        else null
    }.flatten()
}
