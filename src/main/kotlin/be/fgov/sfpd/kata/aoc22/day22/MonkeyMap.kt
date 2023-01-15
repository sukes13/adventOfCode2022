package be.fgov.sfpd.kata.aoc22.day22

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.MoveCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand.TurnLeftCommand
import be.fgov.sfpd.kata.aoc22.day22.BoardCommand.TurnCommand.TurnRightCommand
import be.fgov.sfpd.kata.aoc22.day22.FacingDirection.*
import be.fgov.sfpd.kata.aoc22.day22.TileType.EMPTY
import be.fgov.sfpd.kata.aoc22.day22.TileType.WALL
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String, sideSize: Int): Long {
    val (board, commands) = input.toBoard(sideSize)

    return Explorer.ExplorerFlat(board.startPosition, EAST).tracePath(board, commands, listOf()).password
}

fun part2(input: String, inputType: Pair<Int, List<CubeSideChange>>): Long {
    val (board, commands) = input.toBoard(inputType.first)

    return Explorer.CubeExplorer(board.startPosition, EAST).tracePath(board, commands, inputType.second).password
}


sealed class Explorer(position: Point, facing: FacingDirection) {
    abstract fun turn(command: TurnCommand): Explorer
    abstract fun move(steps: Int, board: Board, cubeSideChanges: List<CubeSideChange>): Explorer

    val password: Long = 1000L * position.y + 4 * position.x + facing.score

    fun tracePath(board: Board, commands: List<BoardCommand>, cubeSidesChanges: List<CubeSideChange>): Explorer =
            commands.fold(this) { explorer : Explorer, command ->
                explorer.execute(command, board,cubeSidesChanges)
            }

    private fun execute(command: BoardCommand, board: Board, cubeSideChanges: List<CubeSideChange>) = when (command) {
        is TurnCommand -> turn(command)
        is MoveCommand -> move(command.steps, board, cubeSideChanges)
    }

    data class ExplorerFlat(val position: Point, val facing: FacingDirection) : Explorer(position, facing) {
        override fun turn(command: TurnCommand) = copy(facing = command.turnFrom(facing))

        override fun move(steps: Int, board: Board, cubeSideChanges: List<CubeSideChange>): ExplorerFlat {
            val line = board.tiles.findLineFor(facing, position)

            return (1..steps).fold(this) { explorer, _ ->
                val nextTile = when (explorer.facing) {
                    NORTH -> line.singleOrNull { it.point.y == explorer.position.y - 1 } ?: line.maxBy { it.point.y }
                    EAST -> line.singleOrNull { it.point.x == explorer.position.x + 1 } ?: line.minBy { it.point.x }
                    SOUTH -> line.singleOrNull { it.point.y == explorer.position.y + 1 } ?: line.minBy { it.point.y }
                    WEST -> line.singleOrNull { it.point.x == explorer.position.x - 1 } ?: line.maxBy { it.point.x }
                }
                when (nextTile.type) {
                    EMPTY -> copy(position = nextTile.point)
                    else -> return explorer
                }
            }
        }
    }

    data class CubeExplorer(val position: Point, val facing: FacingDirection) : Explorer(position, facing) {
        override fun turn(command: TurnCommand) = copy(facing = command.turnFrom(facing))

        override fun move(steps: Int, board: Board, cubeSideChanges: List<CubeSideChange>): CubeExplorer {
            val currentSideNr = board.tiles.single { it.point == position }.sideNr
            val currentSide = board.tiles.filter { it.sideNr == currentSideNr }
            var currentFacing = facing
            var currentLine = currentSide.findLineFor(currentFacing, position)

            return (1..steps).fold(this) { explorer, _ ->
                var nextTile = currentLine.nextTileFor(currentFacing, explorer.position)

                if (nextTile == null) {
                    val sideChange = cubeSideChanges.single { it.sideNr == currentSideNr && it.oldDirection == currentFacing }
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
    }

    fun List<BoardTile>.findLineFor(facing: FacingDirection, position: Point): List<BoardTile> =
            if (facing.isVertical()) filter { it.point.x == position.x } else filter { it.point.y == position.y }
}


data class Board(val tiles: List<BoardTile>, val sideSize: Int) {
    val startPosition = tiles.filter { it.point.y == 1 }.minBy { it.point.x }.point
}

sealed class BoardCommand {

    data class MoveCommand(val steps: Int) : BoardCommand()

    sealed class TurnCommand : BoardCommand() {
        abstract fun turnFrom(facing: FacingDirection): FacingDirection

        object TurnLeftCommand : TurnCommand() {
            override fun turnFrom(facing: FacingDirection) = when (facing) {
                NORTH -> WEST
                WEST -> SOUTH
                SOUTH -> EAST
                EAST -> NORTH
            }
        }

        object TurnRightCommand : TurnCommand() {
            override fun turnFrom(facing: FacingDirection) = when (facing) {
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

//Parsing...
internal fun String.toBoard(sideSize: Int): Pair<Board, List<BoardCommand>> {
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
            sideSize = sideSize
    ) to command.replace("R", ",R,").replace("L", ",L,").split(",").map {
        when (it) {
            "R" -> TurnRightCommand
            "L" -> TurnLeftCommand
            else -> MoveCommand(it.toInt())
        }
    }
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

enum class TileType { WALL, EMPTY }

enum class FacingDirection(val score: Int) {
    NORTH(3), EAST(0), SOUTH(1), WEST(2);

    fun isVertical() = this == NORTH || this == SOUTH
}

