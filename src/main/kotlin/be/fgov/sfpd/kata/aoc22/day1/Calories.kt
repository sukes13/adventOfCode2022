package be.fgov.sfpd.kata.aoc22.day1

import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String) = input.caloriesPerElf().max()

fun part2(input: String) = input.caloriesPerElf().sortedDescending().take(3).sum()

fun String.caloriesPerElf() =
        splitOnEmptyLine().map { perElf ->
            perElf.mapLines { it.toInt() }.sumOf { it }
        }


