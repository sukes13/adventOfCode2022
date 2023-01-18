package be.fgov.sfpd.kata.aoc22.day6

fun part1(input: String) = input.firstMarker(4)

fun part2(input: String) = input.firstMarker(14)

//parsing...
internal fun String.firstMarker(markerSize: Int) =
        this.windowed(markerSize, 1).mapIndexed { index, chunk ->
            when (chunk.toList().distinct().size) {
                markerSize -> index + markerSize
                else -> 0
            }
        }.first { it != 0 }