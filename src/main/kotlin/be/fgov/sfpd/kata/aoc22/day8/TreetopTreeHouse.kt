package be.fgov.sfpd.kata.aoc22.day8

import be.fgov.sfpd.kata.aoc22.*
import be.fgov.sfpd.kata.aoc22.day8.ViewDirection.*

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
    val resultMap = mutableMapOf<ViewDirection, LineOfSight>()
    val column = column(point.x)
    val row = row(point.y)
    ViewDirection.values().forEach { viewDirection ->
        when (viewDirection) {
            TOP -> resultMap[TOP] = column.lookBackwardFrom(point.y)
            RIGHT -> resultMap[RIGHT] = row.lookForwardFrom(point.x)
            BOTTOM -> resultMap[BOTTOM] = column.lookForwardFrom(point.y)
            LEFT -> resultMap[LEFT] = row.lookBackwardFrom(point.x)
        }
    }
    return resultMap.toMap()
}

private fun LineOfSight.lookForwardFrom(target: Int) = this.drop(target + 1)
private fun LineOfSight.lookBackwardFrom(target: Int) = reversed().takeLast(target)
private fun LineOfSight.lowerTreesUntil(treeHeight: Int) = takeWhile { it < treeHeight }.size

fun Point.distanceToEdge(viewDirection: ViewDirection, gridSize: Int) =
        when (viewDirection) {
            TOP -> y
            RIGHT -> gridSize - x - 1
            BOTTOM -> gridSize - y - 1
            LEFT -> x
        }

fun String.toTreetopGrid() =
        lines().mapIndexed { index, line ->
            index to line.toList().map { it.digitToInt() }
        }.toMap()

enum class ViewDirection {
    TOP, RIGHT, BOTTOM, LEFT
}
typealias LineOfSight = List<Int>
typealias LineOfSightMap = Map<ViewDirection, LineOfSight>
