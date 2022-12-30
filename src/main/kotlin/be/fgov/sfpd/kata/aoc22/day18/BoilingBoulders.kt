package be.fgov.sfpd.kata.aoc22.day18

import be.fgov.sfpd.kata.aoc22.flatMapLines

fun part1(input: String) = input.toDroplets().countFreeSides()

fun part2(input: String) = input.toDroplets().countOuterSides()

private fun List<Droplet>.countFreeSides() = sumOf { droplet -> (6 - droplet.sides().count { it in this }) }

private fun List<Droplet>.countOuterSides() = enclosingCube().dijkstraScanForOuterSidesOf(this)

private fun List<Droplet>.dijkstraScanForOuterSidesOf(droplets: List<Droplet>): Int {
    val queue = associate { droplet ->
        if (droplet == first()) droplet to 0 else droplet to Int.MAX_VALUE
    }.toMutableMap()

    var outerSides = 0

    while (queue.isNotEmpty()) {
        val current = queue.filterNot { it.value == Int.MAX_VALUE }
                .minByOrNull { it.value }
                ?: return outerSides

        queue.remove(current.key)

        current.key.accessibleNeighbours(queue)
                .forEach { neighbour ->
                    when (neighbour) {
                        in droplets -> outerSides += 1
                        else -> queue[neighbour] = current.value + 1
                    }
                }
    }

    error("No droplets found...")
}

private fun List<Droplet>.enclosingCube() =
        (minOf { it.x } - 1..maxOf { it.x } + 1).flatMap { x ->
            (minOf { it.y } - 1..maxOf { it.y } + 1).flatMap { y ->
                (minOf { it.z } - 1..maxOf { it.z } + 1).mapNotNull { z ->
                    Droplet(x, y, z)
                }
            }
        }

data class Droplet(val x: Int, val y: Int, val z: Int) {
    fun sides() = listOf(
            this.copy(x = x + 1), this.copy(x = x - 1),
            this.copy(y = y + 1), this.copy(y = y - 1),
            this.copy(z = z + 1), this.copy(z = z - 1)
    )

    fun accessibleNeighbours(queue: MutableMap<Droplet, Int>) = queue.keys.filter { it in sides() }
}

private fun String.toDroplets(): List<Droplet> = flatMapLines {
    it.split(",").map(String::toInt).windowed(3).map { (x, y, z) -> Droplet(x, y, z) }
}