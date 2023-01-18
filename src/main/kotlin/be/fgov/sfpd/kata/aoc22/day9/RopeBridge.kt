package be.fgov.sfpd.kata.aoc22.day9

import be.fgov.sfpd.kata.aoc22.*
import be.fgov.sfpd.kata.aoc22.Direction.*

typealias Rope = List<Point>

fun part1(input: String) = input.toRopeMoves().executeMoves(listOf(Point(0, 0), Point(0, 0))).countUniqueTails()

fun part2(input: String) = input.toRopeMoves().executeMoves(MutableList(10) { Point(0, 0) }.toList()).countUniqueTails()

fun List<RopeMove>.executeMoves(rope: Rope) =
        fold(mutableListOf(rope)) { allStepsExecuted, move ->
            allStepsExecuted += allStepsExecuted.last().allStepsAfter(move)
            allStepsExecuted
//          .also { println("\n$move\n" + allStepsExecuted.last().visualize(30, 10)) }
        }.toList()

private fun List<Rope>.countUniqueTails() = map { it.last() }.also { println(it.visualize(30, 30)) }.distinct().count()

fun Rope.allStepsAfter(move: RopeMove) =
        (0 until move.distance).fold(mutableListOf(this)) { allSteps, _ ->
            allSteps += allSteps.last().stepTowards(move.direction)
            allSteps
        }.toList()

fun List<Point>.visualize(gridWidth: Int, gridHeight: Int): String =
        (gridHeight downTo -gridHeight).map { y ->
            (-gridWidth until gridWidth).mapNotNull { x ->
                when {
                    Point(x, y) in this -> "#"
                    Point(x, y) == Point(0, 0) -> "s"
                    else -> "."
                }
            }
        }.joinToString("\n") { it.joinToString("") }

private fun List<Point>.stepTowards(direction: Direction): List<Point> {
    val movingRope = toMutableList().moveHead(direction)

    (1 until movingRope.size).forEach { index ->
        val previousKnot = movingRope[index - 1]
        val knot = movingRope[index]
        movingRope[index] = when {
            previousKnot.touching(knot) -> knot
            else -> knot.follow(previousKnot)
        }
    }
    return movingRope.toList()
}

private fun MutableList<Point>.moveHead(direction: Direction) = also { this[0] = first().stepInDirection(direction) }

private fun Point.follow(head: Point) = when {
    this onSameColumnAs head -> if (this lowerThan head) stepInDirection(DOWN) else stepInDirection(UP)
    this onSameRowAs head -> if (this moreToLeft head) stepInDirection(LEFT) else stepInDirection(RIGHT)
    else -> when {
        head moreToLeft this && head lowerThan this -> stepInDirection(RIGHT).stepInDirection(UP)
        head moreToLeft this -> stepInDirection(RIGHT).stepInDirection(DOWN)
        head lowerThan this -> stepInDirection(LEFT).stepInDirection(UP)
        else -> stepInDirection(LEFT).stepInDirection(DOWN)
    }
}

data class RopeMove(val direction: Direction, val distance: Int)

//parsing...
internal fun String.toRopeMoves() = mapLines {
    it.trim().split(" ").let { (direction, distance) -> RopeMove(direction.getByLetter(), distance.trim().toInt()) }
}

private fun String.getByLetter() = when (this) {
    "U" -> UP
    "R" -> RIGHT
    "D" -> DOWN
    "L" -> LEFT
    else -> error("Direction: $this does not exist")
}