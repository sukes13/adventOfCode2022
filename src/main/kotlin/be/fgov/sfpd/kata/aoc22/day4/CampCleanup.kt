package be.fgov.sfpd.kata.aoc22.day4

import be.fgov.sfpd.kata.aoc22.filterLines

typealias Sweep = Set<Int>

fun part1(input: String) =
        input.filterLines {
            it.toSweeps().isFullyContaining()
        }.count()

fun part2(input: String) =
        input.filterLines {
            it.toSweeps().overlap().isNotEmpty()
        }.count()

private fun List<Set<Int>>.isFullyContaining() = overlap().let { overlap -> any { it == overlap } }

fun List<Sweep>.overlap() = first() intersect last()

fun String.toSweeps() = split(",").map {
    it.split("-").map(String::toInt).let { (first, last) -> (first..last).toSet() }
}


