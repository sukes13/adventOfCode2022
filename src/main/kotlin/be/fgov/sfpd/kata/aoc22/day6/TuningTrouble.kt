package be.fgov.sfpd.kata.aoc22.day6

fun part1(input: String) = input.firstMarker(4)

fun part2(input: String) = input.firstMarker(14)


fun String.firstMarker(markerLength: Int) =
        this.windowed(markerLength, 1).toList()
            .mapIndexed { index, chunk ->
                when (chunk.toList().distinct().size) {
                    markerLength -> index + markerLength
                    else -> 0
                }
            }.first { it != 0 }