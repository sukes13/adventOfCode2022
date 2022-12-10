package be.fgov.sfpd.kata.aoc22.day9

import be.fgov.sfpd.kata.aoc22.*
import be.fgov.sfpd.kata.aoc22.Direction.*

fun part1(input: String) = Rope().executeMoves(input.toRopeMoves()).countUniqueTails()

private val ropeOfTen = Rope(MutableList(10) { Point(0, 0) }.toList())
fun part2(input: String) = ropeOfTen.executeMoves(input.toRopeMoves()).countUniqueTails()

private fun List<Rope>.countUniqueTails() = map { it.tail }.also { println(it.visualize(30, 30)) }.distinct().count()

data class Rope(val knots: List<Point> = listOf(Point(0, 0), Point(0, 0))) {
    val tail: Point get() = knots.last()

    fun executeMoves(moves: List<RopeMove>) =
            moves.fold(mutableListOf(this)) { allStepsExecuted, move ->
                allStepsExecuted += allStepsExecuted.last().allStepsOf(move)
                allStepsExecuted
//                        .also { println("\n$move\n" + allStepsExecuted.last().visualize(30, 10)) }
            }.toList()

    private fun allStepsOf(move: RopeMove) =
            (0 until move.distance).fold(mutableListOf(this)) { allRopes, _ ->
                val movingRope = allRopes.last().knots.toMutableList().moveHead(move)

                (1 until movingRope.size).forEach { index ->
                    val previousKnot = movingRope[index - 1]
                    val knot = movingRope[index]
                    movingRope[index] = when {
                        previousKnot.touching(knot) -> knot
                        else -> followHead(knot, previousKnot)
                    }
                }

                allRopes += Rope(movingRope)
                allRopes
            }.toList()

    private fun MutableList<Point>.moveHead(move: RopeMove) = also { this[0] = first().stepInDirection(move.direction) }

    private fun followHead(tail: Point, head: Point) = when {
        tail onSameColumnAs head -> if (tail lowerThan head) tail.stepInDirection(DOWN) else tail.stepInDirection(UP)
        tail onSameRowAs head -> if (tail leftOf head) tail.stepInDirection(LEFT) else tail.stepInDirection(RIGHT)
        else -> when {
            head leftOf tail && head lowerThan tail -> tail.stepInDirection(RIGHT).stepInDirection(UP)
            head leftOf tail -> tail.stepInDirection(RIGHT).stepInDirection(DOWN)
            head lowerThan tail -> tail.stepInDirection(LEFT).stepInDirection(UP)
            else -> tail.stepInDirection(LEFT).stepInDirection(DOWN)
        }
    }

    fun visualize(gridWidth: Int, gridHeight: Int) =
            (gridHeight downTo -gridHeight).map { y ->
                (-gridWidth until gridWidth).mapNotNull { x ->
                    when {
                        Point(x, y) in knots && Point(x, y) == knots.first() -> "H"
                        Point(x, y) in knots && Point(x, y) == tail -> "#"
                        Point(x, y) in knots -> "${knots.indexOf(Point(x, y))}"
                        Point(x, y) == Point(0, 0) -> "s"
                        else -> "."
                    }
                }
            }.joinToString("\n") { it.joinToString("") }
}

data class RopeMove(val direction: Direction, val distance: Int)

fun String.toRopeMoves() =
        mapLines {
            it.trim().split(" ").let { (direction, distance) -> RopeMove(direction.getByLetter(), distance.trim().toInt()) }
        }

private fun String.getByLetter() =
        when (this) {
            "U" -> UP
            "R" -> RIGHT
            "D" -> DOWN
            "L" -> LEFT
            else -> error("Direction: $this does not exist")
        }