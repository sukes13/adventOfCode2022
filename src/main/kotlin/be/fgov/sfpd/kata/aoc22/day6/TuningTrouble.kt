package be.fgov.sfpd.kata.aoc22.day6

fun part1(input: String) = input.firstMaker(4)

fun part2(input: String) = input.firstMaker(14)


fun String.firstMaker(markerLength: Int = 14): Int {
    return this.windowed(markerLength,1).toList().mapIndexed  { index, chunk ->
        if(chunk.toList().distinct().size == markerLength) index
        else 0

    }.filterNot { it == 0 }.first() + markerLength

}