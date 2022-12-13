package be.fgov.sfpd.kata.aoc22.day12

import be.fgov.sfpd.kata.aoc22.Point

fun part1(input: String) = input.toHeightMap().simplifiedDijkstraScan('S', 'E') { currentHeight, nextHeight ->
    currentHeight + 1 >= nextHeight
}

fun part2(input: String) = input.toHeightMap().simplifiedDijkstraScan('E', 'a') { currentHeight, nextHeight ->
    currentHeight <= nextHeight + 1
}

private fun List<Height>.simplifiedDijkstraScan(start: Char, end: Char, moveChecker: (Int, Int) -> Boolean): Int {
    val queue = toStartQueue(start)

    while (queue.isNotEmpty()) {
        val current = queue.filterNot { it.value == Int.MAX_VALUE }
                .minByOrNull { it.value }
                ?.also { queue.remove(it.key) } ?: error("No minimal found, destination unreachable...")

        current.key.accessibleNeighbours(queue, moveChecker)
                .forEach { neighbour ->
                    val totalStepsToNeighbour = current.value + 1
                    queue[neighbour] = totalStepsToNeighbour
                    if (neighbour.level == end)
                        return totalStepsToNeighbour
                }
    }

    error("Destination unreachable...")
}

private fun List<Height>.toStartQueue(start: Char) =
        associate { height ->
            when (height.level) {
                start -> height to 0
                else -> height to Int.MAX_VALUE
            }
        }.toMutableMap()


private fun String.toHeightMap() =
        lines().flatMapIndexed { row, line ->
            line.trim().mapIndexed { col, level ->
                Height(location = Point(col, row), level = level)
            }
        }

private data class Height(val location: Point, val level: Char) {
    private val levelHeight = when (level) {
        'S' -> 0
        'E' -> 25
        else -> "abcdefghijklmnopqrstuvwxyz".indexOf(level)
    }

    fun accessibleNeighbours(queue: MutableMap<Height, Int>, upOrDown: (Int, Int) -> Boolean): List<Height> =
            queue.keys.filter {
                it.location in location.orthogonalNeighbours && upOrDown(levelHeight, it.levelHeight)
            }.toList()
}