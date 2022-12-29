package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day14.CavePoints.lineTo
import be.fgov.sfpd.kata.aoc22.day14.CavePoints.pointLeftUnder
import be.fgov.sfpd.kata.aoc22.day14.CavePoints.pointRightUnder
import be.fgov.sfpd.kata.aoc22.day14.CavePoints.pointUnder
import be.fgov.sfpd.kata.aoc22.day14.Filler.ROCK
import be.fgov.sfpd.kata.aoc22.day14.Filler.SAND
import be.fgov.sfpd.kata.aoc22.flatMapLines

val dropPoint = Point(500, 0)

val fillStopperForPart1: (Cave, Int) -> Boolean = { cave, theAbyss -> cave.filterKeys { it.y == theAbyss }.isNotEmpty() }
fun part1(input: String) = input.toCave().fillUp(dropPoint, fillStopperForPart1)
        .filter { it.value == SAND }.count() - 1

val fillStopperForPart2: (Cave, Int) -> Boolean = { cave, _ -> cave.containsKey(dropPoint) }
fun part2(input: String) = input.toCave().fillUp(dropPoint, fillStopperForPart2)
        .filter { it.value == SAND }.count()

fun Cave.fillUp(dropPoint: Point, fillStopper: (Cave, Int) -> Boolean): Cave {
    val bottom = keys.maxBy { it.y }.y + 1
    var cave = this

    while (!fillStopper(cave, bottom)) {
        cave = cave.addUnitOfSand(dropPoint, bottom)
    }

//    println(cave.visualize())
    return cave
}

fun Cave.addUnitOfSand(dropPoint: Point, bottom: Int): Cave {
    var sand = dropPoint
    var dropEnded = false

    while (!dropEnded) {
        sand = if (this[sand.pointUnder()] == null) {
            when (sand.y) {
                bottom -> sand.also { dropEnded = true }
                else -> sand.pointUnder()
            }
        } else {
            when {
                this[sand.pointLeftUnder()] == null -> sand.pointLeftUnder()
                this[sand.pointRightUnder()] == null -> sand.pointRightUnder()
                else -> sand.also { dropEnded = true }
            }
        }
    }

    return toMutableMap().plus(sand to SAND).toMap()
}


fun Cave.visualize(): String {
    val minX = keys.minBy { it.x }.x
    val maxX = keys.maxBy { it.x }.x
    val maxY = keys.maxBy { it.y }.y
    return (0..maxY).joinToString("\n") { y ->
        (minX..maxX).map { x ->
            this[Point(x, y)] ?: if (Point(x, y) == dropPoint) "+" else "."
        }.joinToString("")
    }
}

fun String.toCave(): Cave = flatMapLines { it.toRockLine() }.associateWith { ROCK }

fun String.toRockLine(): List<Point> =
        split(" -> ").windowed(2, 1).flatMap { (start, end) ->
            start.splitToPoint() lineTo end.splitToPoint()
        }

fun String.splitToPoint() = split(",").windowed(2).map { (x, y) -> Point(x.toInt(), y.toInt()) }.single()

object CavePoints {
    infix fun Point.lineTo(end: Point) = if (x == end.x) verticalLineTo(end) else horizontalLineTo(end)

    fun Point.pointUnder(): Point = this + Point(0, 1)
    fun Point.pointLeftUnder(): Point = this + Point(-1, 1)
    fun Point.pointRightUnder(): Point = this + Point(1, 1)

    private fun Point.horizontalLineTo(end: Point) = when {
        x < end.x -> (x..end.x).map { Point(it, y) }
        else -> (end.x..x).map { Point(it, y) }
    }

    private fun Point.verticalLineTo(end: Point) = when {
        y < end.y -> (y..end.y).map { Point(x, it) }
        else -> (end.y..y).map { Point(x, it) }
    }
}

typealias Cave = Map<Point, Filler>

enum class Filler(private val sign: String) {
    ROCK("#"),
    SAND("o");

    override fun toString() = sign
}