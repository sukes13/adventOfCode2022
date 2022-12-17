package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day14.CaveElements.lineTo
import be.fgov.sfpd.kata.aoc22.day14.Filler.ROCK
import be.fgov.sfpd.kata.aoc22.day14.Filler.SAND
import be.fgov.sfpd.kata.aoc22.flatMapLines

fun part1(input: String) = input.toCave().fillUp().filter { it.value == SAND }.count() - 1
fun part2(input: String) = ""

private val dropPoint = Point(500, 0)

fun Cave.fillUp(): Cave{
    var overFlowing = false
    var cave = this

    while (!overFlowing){
        cave = cave.addUnitOfSand().let { (newCave, keepsDropping) ->
            newCave.also { overFlowing = keepsDropping }
        }
    }
    return cave.also { println(it.visualize()) }
}

fun Cave.addUnitOfSand(): Pair<Cave,Boolean> {
    val theAbyss = this.keys.maxBy { it.y }.y + 2
    var sand = dropPoint
    var dropEnded = false

    while(!dropEnded){
        sand = when {
            this[sand.pointUnder()] != null -> {
                when {
                    this[sand.pointLeftUnder()] == null -> sand.pointLeftUnder()
                    this[sand.pointRightUnder()] == null -> sand.pointRightUnder()
                    else -> sand.also { dropEnded = true }
                }
            }
            sand.y == theAbyss -> sand.also { dropEnded = true }
            else -> sand.pointUnder()
        }
    }

    return toMutableMap().plus(sand to SAND).toMap() to (sand.y == theAbyss)
}

private fun Point.pointUnder(): Point = this + Point(0, 1)
private fun Point.pointLeftUnder(): Point = this + Point(-1, 1)
private fun Point.pointRightUnder(): Point = this + Point(1, 1)

fun Cave.visualize(): String {
    val minX = keys.minBy { it.x }.x
    val maxX = keys.maxBy { it.x }.x
    val maxY = keys.maxBy { it.y }.y
    return (0 .. maxY).joinToString("\n") { y ->
        (minX .. maxX).map { x ->
            when (Point(x, y)) {
                dropPoint -> "+"
                else -> this[Point(x, y)] ?: "."
            }
        }.joinToString("")
    }
}

fun String.toCave(): Cave = flatMapLines { it.toRockLine() }.associateWith { ROCK }

fun String.toRockLine(): List<Point> =
        trim().split(" -> ").windowed(2, 1).flatMap { (start, end) ->
            start.splitToPoint() lineTo end.splitToPoint()
        }

fun String.splitToPoint(delimiter: String = ",") = trim().split(delimiter).windowed(2).map { (x, y) -> Point(x.toInt(), y.toInt()) }.single()

object CaveElements {
    infix fun Point.lineTo(end: Point) = when {
        end == this -> listOf(this)
        x == end.x -> verticalLineTo(end)
        else -> horizontalLineTo(end)
    }

    private fun Point.horizontalLineTo(end: Point) = when {
        x < end.x -> (x .. end.x).map { Point(it,y) }
        else -> (end.x .. x ).map { Point(it,y) }
    }

    private fun Point.verticalLineTo(end: Point) = when {
        y < end.y -> (y..end.y).map { Point(x, it) }
        else -> (end.y .. y ).map { Point(x, it) }
    }
}

typealias Cave = Map<Point, Filler>

enum class Filler(private val sign: String) {
    ROCK("#"),
    SAND("o");

    override fun toString() = sign
}