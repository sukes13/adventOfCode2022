package be.fgov.sfpd.kata.aoc22.day1

import be.fgov.sfpd.kata.aoc22.mapLines

fun part1(input: String) = input.caloriesPerElf().max()

fun part2(input: String) = input.caloriesPerElf().sortedDescending().take(3).sum()

fun String.caloriesPerElf() =
        split("\r\n\r\n").map { perElf ->
            perElf.mapLines { it.toInt() }.sumOf { it }
        }


