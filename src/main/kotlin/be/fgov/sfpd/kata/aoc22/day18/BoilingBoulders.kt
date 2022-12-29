package be.fgov.sfpd.kata.aoc22.day18

import be.fgov.sfpd.kata.aoc22.flatMapLines

fun part1(input: String) = input.toDroplets().countFreeSides()

private fun List<Droplet>.countFreeSides() =
        sumOf { droplet ->
            (6 - droplet.sides().count { it in this })
        }

fun part2(input: String) = ""

data class Droplet(val x: Int, val y: Int, val z: Int) {
    fun sides() = listOf(
            this.copy(x = x + 1), this.copy(x = x - 1),
            this.copy(y = y + 1), this.copy(y = y - 1),
            this.copy(z = z + 1), this.copy(z = z - 1)
    )
}

private fun String.toDroplets(): List<Droplet> = flatMapLines {
    it.split(",").map(String::toInt).windowed(3).map { (x, y, z) -> Droplet(x, y, z) }
}