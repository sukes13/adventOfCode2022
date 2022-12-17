package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day14.CaveElements.lineTo
import be.fgov.sfpd.kata.aoc22.day14.Filler.ROCK
import be.fgov.sfpd.kata.aoc22.flatMapLines
import be.fgov.sfpd.kata.aoc22.lowerThan
import be.fgov.sfpd.kata.aoc22.moreToLeft

fun part1(input: String) = ""
fun part2(input: String) = ""

fun Cave.visualize() =
        (0 until 10).joinToString("\n") { y ->
            "$y " + (494 until 504).map { x ->
                when {
                    Point(x, y) == Point(500, 0) -> "+"
                    else -> this.firstOrNull { it atLocation Point(x, y) }?.filler ?: "."
                }
            }.joinToString("")
        }

fun String.toCave(): List<CaveTile> = flatMapLines { it.toRockLine() }.also { println(it.visualize()) }

fun String.toRockLine(): List<CaveTile> =
        trim().split(" -> ").windowed(2, 1).flatMap { (start, end) ->
            (start.splitToPoint() lineTo end.splitToPoint()).map { CaveTile(it, ROCK) }
        }.distinct()

fun String.splitToPoint(delimiter: String = ",") = trim().split(delimiter).windowed(2).map { (x, y) -> Point(x.toInt(), y.toInt()) }.single()

object CaveElements {
    infix fun Point.lineTo(end: Point) = when {
        end == this -> listOf(this)
        x == end.x -> verticalLineTo(end)
        else -> horizontalLineTo(end)
    }

    private fun Point.horizontalLineTo(end: Point) = when {
        this lowerThan end -> (x..end.x).map { Point(it, y - 2) }
        else -> (x downTo end.x).map { Point(it, y) }
    }

    private fun Point.verticalLineTo(end: Point) = when {
        end moreToLeft this -> (y downTo end.y).map { Point(x - 2, it) }
        else -> (y..end.y).map { Point(x, it) }
    }
}

data class CaveTile(val point: Point, val filler: Filler) {
    infix fun atLocation(other: Point) = point == other
}

typealias Cave = List<CaveTile>

enum class Filler(private val sign: String) {
    ROCK("#");

    override fun toString() = sign
}