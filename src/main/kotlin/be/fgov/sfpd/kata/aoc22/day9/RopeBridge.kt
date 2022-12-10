package be.fgov.sfpd.kata.aoc22.day9

import be.fgov.sfpd.kata.aoc22.*
import be.fgov.sfpd.kata.aoc22.Direction.*

private val ropeOfTen = Rope(MutableList(10) { Point(0, 0) }.toList())

fun part1(input: String) = Rope().executeMoves(input.toRopeMoves()).countUniqueTails()

fun part2(input: String) = ropeOfTen.executeMoves(input.toRopeMoves()).countUniqueTails()

private fun List<Rope>.countUniqueTails() = map { it.tail }.also { println(it.visualize(30, 30)) }.distinct().count()

data class Rope(val knots: List<Point> = listOf(Point(0, 0), Point(0, 0))) {
    val tail: Point get() = knots.last()

    fun executeMoves(moves: List<RopeMove>) =
            moves.fold(mutableListOf(this)) { ropes, move ->
                ropes += ropes.last().allStepsOf(move)
                ropes
//                        .also { println("\n$move\n" + ropes.last().visualize(30, 10)) }
            }.toList()

    private fun allStepsOf(move: RopeMove) =
            (0 until move.distance).fold(mutableListOf(this)) { allRopes, _ ->
                val movingRope = allRopes.last().knots.toMutableList()
                movingRope.moveHead(move)

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

    private fun MutableList<Point>.moveHead(move: RopeMove) {
        val movedHead = first().stepInDirection(move.direction)
        this[0] = movedHead
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

private fun followHead(tail: Point, head: Point): Point {
    return if (tail.x == head.x) {
        if (tail.y > head.y) tail.stepInDirection(DOWN) else tail.stepInDirection(UP)
    } else if (tail.y == head.y) {
        if (tail.x > head.x) tail.stepInDirection(LEFT) else tail.stepInDirection(RIGHT)
    } else {
        when {
            head.x > tail.x && head.y > tail.y -> tail.stepInDirection(RIGHT).stepInDirection(UP)
            head.x > tail.x -> tail.stepInDirection(RIGHT).stepInDirection(DOWN)
            head.y > tail.y -> tail.stepInDirection(LEFT).stepInDirection(UP)
            else -> tail.stepInDirection(LEFT).stepInDirection(DOWN)
        }
    }
}

data class RopeMove(val direction: Direction, val distance: Int)

fun String.toRopeMoves(): List<RopeMove> =
        mapLines {
            it.trim().split(" ")
                    .let { (direction, distance) -> RopeMove(direction.getByLetter(), distance.trim().toInt()) }
        }

private fun String.getByLetter() =
        when (this) {
            "U" -> UP
            "R" -> RIGHT
            "D" -> DOWN
            "L" -> LEFT
            else -> error("Direction: $this does not exist")
        }