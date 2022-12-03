package be.fgov.sfpd.kata.aoc22.day3

import be.fgov.sfpd.kata.aoc22.day3.Priorities.priorityOf
import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.toChar


fun solution1(input: String) = input.mapLines { it.splitInCompartments().sharedItem() }.sumOf { priorityOf(it) }

fun solution2(input: String) = input.splitGroups().sumOf { priorityOf(it.sharedItem()) }

fun List<String>.sharedItem(): String =
        drop(1).fold(first().toList()) { shared, rucksack ->
            shared.sharesWith(rucksack)
        }.joinToString("")

private fun List<Char>.sharesWith(rucksack: String) =
        rucksack.chunked(1)
                .map(String::toChar)
                .filter { it in this }
                .distinct()

object Priorities {
    private val scores = (('a'..'z') + ('A'..'Z')).zip((1..52)).toMap()

    fun priorityOf(item: String) = scores[item.toChar()] ?: 0
}

fun String.splitInCompartments() = this.chunked(this.length / 2)
fun String.splitGroups() = lines().chunked(3)
