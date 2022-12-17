package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day14.CaveElements.lineTo
import be.fgov.sfpd.kata.aoc22.day14.Filler.ROCK
import be.fgov.sfpd.kata.aoc22.lowerThan
import be.fgov.sfpd.kata.aoc22.moreToLeft

fun part1(input: String) = ""
fun part2(input: String) = ""

fun Cave.visualize(): String {
    val width = maxBy { it.point.x }
    return groupBy { it.point.y }.toSortedMap().map { (row , cavePoint) ->
        "$row" + cavePoint.map { it.filler }
    }.joinToString("\n")
//        return (highestStack downTo 1).fold("\n") { image, level ->
//            image + this.values.joinToString("") { stack ->
//                stack.getOrNull(level - 1)?.let { "[${stack[stack.size - level]}] " } ?: "    "
//            } + "\n"
//        }.trimEnd()
//
//    return ""
}

fun String.toRockLine(): List<CaveTile> =
        trim().split(" -> ").windowed(2, 1).flatMap { (start, end) ->
            (start.splitToPoint() lineTo end.splitToPoint()).map { CaveTile(it, ROCK) }
        }.distinct().also { println( it.visualize() )}

fun String.splitToPoint(delimiter: String = ",") = split(delimiter).windowed(2).map { (x, y) -> Point(x.toInt(), y.toInt()) }.single()

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

data class CaveTile(val point: Point, val filler: Filler)

typealias Cave = List<CaveTile>

enum class Filler { ROCK }