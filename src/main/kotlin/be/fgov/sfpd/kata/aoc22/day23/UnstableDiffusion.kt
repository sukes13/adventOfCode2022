package be.fgov.sfpd.kata.aoc22.day23

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day23.Direction.*
import java.time.LocalDateTime


fun part1(input: String) = input.toElves().spreadOut(10, NORTH).let { (elves, _) ->
    (elves.width() * elves.height()) - elves.size
}

fun part2(input: String) = input.toElves().spreadOut(Int.MAX_VALUE, NORTH).second

internal fun List<Point>.spreadOut(totalRounds: Int, direction: Direction, round : Int = 1): Pair<List<Point>, Int> {
    if( round > totalRounds) return this to round

    println("Round nr: $round at ${LocalDateTime.now()}")

    return moveIfPossibleOrNullFrom(direction)?.spreadOut(totalRounds, direction.next(), round + 1) ?: (this to round)
}

fun List<Point>.moveIfPossibleOrNullFrom(direction: Direction): List<Point>? {
    val proposals = considerPositionsStartingFrom(direction)

    val elves = this.toMutableList()
    var moveCount = 0

    proposals.filter { proposal ->
        proposals.count { proposal.second == it.second } <= 1
    }.forEach { (elf, newPosition) ->
        elves.remove(elf)
        elves.add(newPosition)
        moveCount += 1
    }

    return if (moveCount == 0) null else elves
}

fun List<Point>.considerPositionsStartingFrom(direction: Direction) = mapNotNull { elf ->
    if (elf.neighbours.any { it in this }) {
        proposedPositionFor(elf, direction)?.let {
            elf to it
        }
    } else null
}

private fun List<Point>.proposedPositionFor(elf: Point, startDirection: Direction): Point? {
    startDirection.startFrom().forEach { direction ->
        if (elf.neighboursToThe(direction).none { it in this }) {
            return elf.stepTo(direction)
        }
    }
    return null
}

private fun Point.stepTo(direction: Direction) = when (direction) {
    NORTH -> this + Point(0, -1)
    SOUTH -> this + Point(0, 1)
    WEST -> this + Point(-1, 0)
    EAST -> this + Point(1, 0)
}

private fun Point.neighboursToThe(direction: Direction) = when (direction) {
    NORTH -> listOf(Point(-1, -1), Point(0, -1), Point(1, -1))
    SOUTH -> listOf(Point(-1, 1), Point(0, 1), Point(1, 1))
    WEST -> listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1))
    EAST -> listOf(Point(1, -1), Point(1, 0), Point(1, 1))
}.map { vector -> this + vector }.toSet()

internal fun List<Point>.visualize(): String {
    val minX = minBy { it.x }.x
    val maxX = maxBy { it.x }.x
    val minY = minBy { it.y }.y
    val maxY = maxBy { it.y }.y

    return (minY..maxY).joinToString("\n") { y ->
        (minX..maxX).joinToString("") { x ->
            when {
                Point(x, y) in this -> "#"
                else -> "."
            }
        }
    }
}

private fun List<Point>.width() = maxBy { it.x }.x - minBy { it.x }.x + 1
private fun List<Point>.height() = maxBy { it.y }.y - minBy { it.y }.y + 1

enum class Direction {
    NORTH, SOUTH, WEST, EAST;

    fun next() = values()[(ordinal + 1) % values().size]
    fun startFrom() = values().drop(ordinal) + values().take(ordinal)
}

//Parsing...
internal fun String.toElves() = lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, char ->
        when (char) {
            '#' -> Point(x, y)
            else -> null
        }
    }.filterNotNull()
}

