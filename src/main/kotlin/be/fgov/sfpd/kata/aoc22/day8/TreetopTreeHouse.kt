package be.fgov.sfpd.kata.aoc22.day8

import be.fgov.sfpd.kata.aoc22.*
import be.fgov.sfpd.kata.aoc22.Direction.*

fun part1(input: String) = input.toTreetopGrid().checkEveryPointFor(isVisibleFromOutsideChecker).filter { it }.size

fun part2(input: String) = input.toTreetopGrid().checkEveryPointFor(scenicScoreChecker).max()

val isVisibleFromOutsideChecker: (Point, Grid<Int>) -> Boolean = { tree, grid ->
    val treeHeight = grid.get(tree)
    grid.linesOfSightOf(tree).map { (direction, lineOfSight) ->
        lineOfSight.lowerTreesUntil(treeHeight) == tree.distanceToEdge(direction, grid.size)
    }.any { it }
}

val scenicScoreChecker: (Point, Grid<Int>) -> Int = { tree, grid ->
    val treeHeight = grid.get(tree)
    grid.linesOfSightOf(tree).map { (direction, lineOfSight) ->
        lineOfSight.lowerTreesUntil(treeHeight).let {
            if (it == tree.distanceToEdge(direction, grid.size)) it else it + 1
        }
    }.reduce { a, b -> a * b }
}

fun Grid<Int>.linesOfSightOf(point: Point): LineOfSightMap {
    val resultMap = mutableMapOf<Direction, LineOfSight>()
    val column = column(point)
    val row = row(point)
    Direction.values().forEach { viewDirection ->
        when (viewDirection) {
            UP -> resultMap[UP] = column.lookBackwardFrom(point.y)
            RIGHT -> resultMap[RIGHT] = row.lookForwardFrom(point.x)
            DOWN -> resultMap[DOWN] = column.lookForwardFrom(point.y)
            LEFT -> resultMap[LEFT] = row.lookBackwardFrom(point.x)
        }
    }
    return resultMap.toMap()
}

private fun LineOfSight.lookForwardFrom(target: Int) = this.drop(target + 1)
private fun LineOfSight.lookBackwardFrom(target: Int) = reversed().takeLast(target)
private fun LineOfSight.lowerTreesUntil(treeHeight: Int) = takeWhile { it < treeHeight }.size

fun Point.distanceToEdge(viewDirection: Direction, gridSize: Int) =
        when (viewDirection) {
            UP -> y
            RIGHT -> gridSize - x - 1
            DOWN -> gridSize - y - 1
            LEFT -> x
        }

fun String.toTreetopGrid() =
        lines().mapIndexed { index, line ->
            index to line.toList().map { it.digitToInt() }
        }.toMap()


typealias LineOfSight = List<Int>
typealias LineOfSightMap = Map<Direction, LineOfSight>
