package be.fgov.sfpd.kata.aoc22.day18

import be.fgov.sfpd.kata.aoc22.flatMapLines


fun part1(input: String) = input.toDroplet().countFreeSides()

fun part2(input: String) = input.toDroplet().countOuterSides()

private fun List<LavaCube>.countFreeSides() = sumOf { lavaCube -> 6 - lavaCube.sides().count { it in this } }

private fun List<LavaCube>.countOuterSides() = enclosingCube().dijkstraScanForOuterSidesOf(this)

private fun List<LavaCube>.dijkstraScanForOuterSidesOf(lavaCubes: List<LavaCube>): Int {
    val queue = associate { lavaCube ->
        if (lavaCube == first()) lavaCube to 0 else lavaCube to Int.MAX_VALUE
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
                        in lavaCubes -> outerSides += 1
                        else -> queue[neighbour] = current.value + 1
                    }
                }
    }

    error("No lava-cubes found...")
}

private fun List<LavaCube>.enclosingCube() =
        (minOf { it.x } - 1..maxOf { it.x } + 1).flatMap { x ->
            (minOf { it.y } - 1..maxOf { it.y } + 1).flatMap { y ->
                (minOf { it.z } - 1..maxOf { it.z } + 1).mapNotNull { z ->
                    LavaCube(x, y, z)
                }
            }
        }

data class LavaCube(val x: Int, val y: Int, val z: Int) {
    fun sides() = listOf(
            this.copy(x = x + 1), this.copy(x = x - 1),
            this.copy(y = y + 1), this.copy(y = y - 1),
            this.copy(z = z + 1), this.copy(z = z - 1)
    )

    fun accessibleNeighbours(dijkstraQueue: MutableMap<LavaCube, Int>) = dijkstraQueue.keys.filter { it in sides() }
}

private fun String.toDroplet(): List<LavaCube> = flatMapLines {
    it.split(",").map(String::toInt).windowed(3).map { (x, y, z) -> LavaCube(x, y, z) }
}