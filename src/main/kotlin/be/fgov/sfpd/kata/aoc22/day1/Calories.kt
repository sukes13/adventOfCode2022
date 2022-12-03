package be.fgov.sfpd.kata.aoc22.day1

import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.spitOnEmptyLine

fun part1(input: String) = input.caloriesPerElf().max()

fun part2(input: String) =
        input.caloriesPerElf()
                .sortedDescending()
                .take(3)
                .sum()

fun String.caloriesPerElf() =
        spitOnEmptyLine().map { perElf ->
            perElf.mapLines { it.toInt() }.sumOf { it }
        }


