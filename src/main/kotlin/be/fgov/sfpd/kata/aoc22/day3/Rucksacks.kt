package be.fgov.sfpd.kata.aoc22.day3

import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.toChar


fun solution1(input: String) = input.mapLines { it.sharedItem() }.sumOf { Priorities.scoreFor(it) }

fun solution2(input: String): Nothing = TODO()

fun String.sharedItem() =
        this.splitInTwo().let { (compartmentOne, compartmentTwo) ->
            compartmentOne.chunked(1)
                    .first { compartmentTwo.contains(it.toChar()) }
        }

object Priorities {
    private val scores = (('a'..'z') + ('A'..'Z')).zip((1..52)).toMap()

    fun scoreFor(item: String) = scores[item.toChar()] ?: 0
}

private fun String.splitInTwo() = this.chunked(this.length / 2)

