package be.fgov.sfpd.kata.aoc22.day23

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day23.Direction.*
import java.time.LocalDateTime


fun part1(input: String) = input.toElves().spreadOut().let { elves ->
    (elves.width() * elves.height()) - elves.size
}


fun part2(input: String) = 0

internal fun List<SowingElf>.spreadOut(): List<SowingElf> {
    var direction = NORTH
    var spreadEnded = false
    var result = this
    var rounds = 10

    while (!spreadEnded && rounds >= 1) {
//        println( result.visualize() + "\n")
        println("Round nr: ${10-rounds} at ${LocalDateTime.now()}")

        result = result.moveRoundIfPossibleOrNull(direction)?.also { direction = direction.next() }
                ?: result.also { spreadEnded = true }
        rounds -= 1
    }
    return result
}

fun List<SowingElf>.moveRoundIfPossibleOrNull(direction: Direction): List<SowingElf>? {
    val proposals = considerPositionsStartingFrom(direction)

    val elves = this.toMutableList()
    proposals.forEach { (elf, newPosition) ->
        if (proposals.count { it.second == newPosition } <= 1) {
            elves.remove(elf)
            elves.add(elf.copy(position = newPosition))
        }
    }

    if (proposals.isEmpty()) return null
    return elves
}

fun List<SowingElf>.considerPositionsStartingFrom(direction: Direction) =
        fold(mutableListOf<Pair<SowingElf, Point>>()) { proposals, elf ->
            if (any { it.position in elf.position.neighbours }) {
                proposedPositionFor(elf, direction)?.let {
                    proposals.plus(elf to it).toMutableList()
                } ?: proposals
            } else proposals
        }.toList()


private fun List<SowingElf>.proposedPositionFor(elf: SowingElf, startDirection: Direction): Point? {
    startDirection.startFrom().forEach { direction ->
        if (none { it.position in elf.position.neighboursToThe(direction) }) {
            return elf.position.stepTo(direction)
        }
    }
    return null
}

private fun Point.stepTo(direction: Direction) = when (direction) {
    NORTH -> this + Point(0, -1)
    SOUTH -> this + Point(0, 1)
    WEST -> this + Point(-1, -0)
    EAST -> this + Point(1, 0)
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST;

    fun next() = values()[(ordinal + 1) % values().size]
    fun startFrom() = values().drop(ordinal) + values().take(ordinal)
}

private fun Point.neighboursToThe(direction: Direction) = when (direction) {
    NORTH -> listOf(Point(-1, -1), Point(0, -1), Point(1, -1))
    SOUTH -> listOf(Point(-1, 1), Point(0, 1), Point(1, 1))
    WEST -> listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1))
    EAST -> listOf(Point(1, -1), Point(1, 0), Point(1, 1))
}.map { vector -> this + vector }.toSet()

internal fun List<SowingElf>.visualize(): String {
    val minX = minBy { it.position.x }.position.x
    val maxX = maxBy { it.position.x }.position.x
    val minY = minBy { it.position.y }.position.y
    val maxY = maxBy { it.position.y }.position.y

    return (minY..maxY).joinToString("\n") { y ->
        (minX..maxX).joinToString("") { x ->
            when {
                Point(x, y) in this.map { it.position } -> "#"
                else -> "."
            }
        }
    }
}

private fun List<SowingElf>.width() = maxBy { it.position.x }.position.x - minBy { it.position.x }.position.x + 1
private fun List<SowingElf>.height() = maxBy { it.position.y }.position.y - minBy { it.position.y }.position.y + 1

data class SowingElf(val id: String, val position: Point)

//Parsing...
internal fun String.toElves() = lines().flatMapIndexed { y, line ->
    line.mapIndexed { x, char ->
        when (char) {
            '#' -> SowingElf("$x$y", Point(x, y))
            else -> null
        }
    }.filterNotNull()
}

