package be.fgov.sfpd.kata.aoc22.day1

fun solution1(input: String) =
    input.caloriesPerElf()
        .max()

fun solution2(input: String) =
    input.caloriesPerElf()
        .sortedDescending()
        .take(3)
        .sum()

fun String.caloriesPerElf() =
    this.split("\r\n\r\n")
        .map { perElf ->
            perElf.lines()
                .sumOf { it.toInt() }
        }
