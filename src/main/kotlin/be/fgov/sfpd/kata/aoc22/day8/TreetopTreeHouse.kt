package be.fgov.sfpd.kata.aoc22.day8

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day8.ViewDirection.*

fun part1(input: String) = input.toTreetopGrid().checkEveryTreeFor(isVisibleFromOutsideChecker).filter { it }.size

fun part2(input: String) = input.toTreetopGrid().checkEveryTreeFor(scenicScoreChecker).max()

val isVisibleFromOutsideChecker: (Point, TreetopGrid) -> Boolean = { point, grid ->
    val treeHeight = grid.heightAt(point)
    grid.linesOfSightOf(point).map { (direction, lineOfSight) ->
        lineOfSight.lowerTreesUntil(treeHeight) == point.distanceToEdge(direction, grid.size)
    }.any { it }
}
val scenicScoreChecker: (Point, TreetopGrid) -> Int = { point, grid ->
    val treeHeight = grid.heightAt(point)
    grid.linesOfSightOf(point).map { (direction, lineOfSight) ->
        lineOfSight.lowerTreesUntil(treeHeight).let {
            if (it == point.distanceToEdge(direction, grid.size)) it else it + 1
        }
    }.reduce { a, b -> a * b }
}

fun TreetopGrid.linesOfSightOf(point: Point): LineOfSightMap {
    val resultMap = mutableMapOf<ViewDirection, LineOfSight>()
    ViewDirection.values().forEach { viewDirection ->
        val row = this.row(point.x)
        when (viewDirection) {
            TOP -> resultMap[TOP] = row.lookBackwardFrom(point.y)
            RIGHT -> resultMap[RIGHT] = column(point).lookForwardFrom(point.x)
            BOTTOM -> resultMap[BOTTOM] = row.lookForwardFrom(point.y)
            LEFT -> resultMap[LEFT] = column(point).lookBackwardFrom(point.x)
        }
    }
    return resultMap.toMap()
}

private fun LineOfSight.lookForwardFrom(target: Int) = this.drop(target + 1)
private fun LineOfSight.lookBackwardFrom(target: Int) = reversed().takeLast(target)
private fun LineOfSight.lowerTreesUntil(treeHeight: Int) = takeWhile { it < treeHeight }.size

private fun <T> TreetopGrid.checkEveryTreeFor(checker: (Point, TreetopGrid) -> T?) =
        (0 until this.size).flatMap { x ->
            (0 until this.size).mapNotNull { y ->
                checker(Point(x, y), this)
            }
        }
private fun TreetopGrid.heightAt(point: Point) = this[point.y]?.get(point.x) ?: error("Point: $point does not exist in grid")
private fun TreetopGrid.row(rowNumber: Int) = values.map { it.elementAt(rowNumber) }
private fun TreetopGrid.column(point: Point) = this[point.y] ?: error("Row: ${point.x} does not exist in grid")

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
typealias TreetopGrid = Map<Int, LineOfSight>
typealias LineOfSightMap = Map<ViewDirection, LineOfSight>
