package be.fgov.sfpd.kata.aoc22.day4

import be.fgov.sfpd.kata.aoc22.filterLines
import be.fgov.sfpd.kata.aoc22.overlap

fun part1(input: String) = input.filterLines { it.toSweeps().isFullyContaining() }.count()

fun part2(input: String) = input.filterLines { it.toSweeps().overlap().isNotEmpty() }.count()

private fun Pair<Set<Int>,Set<Int>>.isFullyContaining() = overlap().let { overlap -> this.toList().any { it == overlap } }

fun String.toSweeps() = split(",").map {
    it.split("-").map(String::toInt).let { (first, last) -> (first..last).toSet() }
}.zipWithNext().single()


