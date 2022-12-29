package be.fgov.sfpd.kata.aoc22.day17

import be.fgov.sfpd.kata.aoc22.day17.GasJet.LEFT
import be.fgov.sfpd.kata.aoc22.day17.GasJet.RIGHT
import be.fgov.sfpd.kata.aoc22.day17.RockShape.*


fun part1(input: String) = Cave(input.toJetPattern()).dropRocks(2022)

fun part2(input: String) = Cave(input.toJetPattern()).dropRocks(1_000_000_000_000L)

typealias CaveLine = List<Boolean>
typealias Rock = List<CaveLine>
typealias RockTower = MutableMap<Long, CaveLine>

data class Cave(val jetPattern: List<GasJet>, val width: Int = 7) {
    private var tower: RockTower = mutableMapOf(0L to ((0 until width).map { true }.toList()))
    private val rockShapes = listOf(HorizontalLine(), PlusSign(), MirroredL(), VerticalLine(), Rectangle())

    fun dropRocks(totalRocks: Long): Long {
        return totalRocks.dropWithPatternDetection()?.let { pattern ->
            val rocksInPattern = pattern.rocksEnd - pattern.rocksStart
            val patternRepeats = (totalRocks - pattern.rocksStart) / rocksInPattern
            val remainingRocks = totalRocks - pattern.rocksStart - (patternRepeats * rocksInPattern)

            remainingRocks.dropWithPatternDetection(pattern.rockShapeIndex.nextOrFirst(rockShapes.size), pattern.gasJetIndex.nextOrFirst(jetPattern.size))

            pattern.height * (patternRepeats - 1) + tower.height()
        } ?: tower.height()
    }

    private fun Long.dropWithPatternDetection(startRockShape: Int = 0, startGasJet: Int = 0): RockPattern? {
        var gasJetIndex = startGasJet
        var rockShapeIndex = startRockShape
        val dropCycles = mutableMapOf<DropCycle, TowerState>()

        (1..this).forEach { rockNumber ->
            gasJetIndex = dropRock(rockShapes[rockShapeIndex], gasJetIndex)

            val dropCycle = DropCycle(rockShapeIndex, gasJetIndex, tower.topNumberOfLines(10).values.toList())
            dropCycles.put(dropCycle, TowerState(rockNumber, tower.height()))?.let { match ->
                println("Pattern detected! $dropCycle")
                return RockPattern(match.rocks, rockNumber, tower.height() - match.height, rockShapeIndex, gasJetIndex)
            }

            tower = tower.skimmedIfSize(100).toMutableMap()

            rockShapeIndex = rockShapeIndex.nextOrFirst(rockShapes.size)
            gasJetIndex = gasJetIndex.nextOrFirst(jetPattern.size)
        }
        return null
    }

    private fun dropRock(rockShape: RockShape, startGasJetIndex: Int): Int {
        var gasJetIndex = startGasJetIndex
        var currentHeight = tower.height() + 4
        var movingRock = rockShape.startRock
        var cameToRest = false

        while (!cameToRest) {
            val blownRock = movingRock.hitWithGasJet(jetPattern[gasJetIndex], currentHeight)

            if (currentHeight <= tower.height() + 1 && blownRock overlapsTowerAt currentHeight - 1) {
                addRockInRest(blownRock, currentHeight)
                cameToRest = true
            } else {
                movingRock = blownRock
                currentHeight -= 1
                gasJetIndex = gasJetIndex.nextOrFirst(jetPattern.size)
            }
        }

        return gasJetIndex
    }

    private fun addRockInRest(movingRock: Rock, rockBottomHeight: Long) {
        movingRock.forEachIndexed { index, rockLine ->
            tower[rockBottomHeight + index] = tower[rockBottomHeight + index]?.mergeWith(rockLine) ?: rockLine
        }
    }

    private fun RockTower.height() = maxBy { it.key }.key

    private fun RockTower.topNumberOfLines(numberOfLines: Int) = filterKeys { it > height() - numberOfLines }

    private fun RockTower.skimmedIfSize(maxSize: Int) = if (size > maxSize) topNumberOfLines(30) else this

    private fun Rock.hitWithGasJet(gasJet: GasJet, currentHeight: Long): Rock =
            when (gasJet) {
                LEFT -> moveLeftIfNoWall()
                RIGHT -> moveRightIfNoWall()
            }.let { movedRock ->
                if (movedRock overlapsTowerAt currentHeight) this else movedRock
            }

    private fun Rock.moveRightIfNoWall() = if (map { it.last() }.any { it }) this else map { listOf(false) + it.dropLast(1) }

    private fun Rock.moveLeftIfNoWall() = if (map { it.first() }.any { it }) this else map { it.drop(1) + false }

    private infix fun Rock.overlapsTowerAt(rockBottomHeight: Long) =
            filterIndexed { index, rockLine ->
                tower[rockBottomHeight + index]?.let { towerLine ->
                    rockLine overlaps towerLine
                } ?: false
            }.isNotEmpty()

    private fun Int.nextOrFirst(total: Int): Int = if (this + 1 >= total) 0 else this + 1

    private infix fun CaveLine.overlaps(other: CaveLine) = zip(other).any { (a, b) -> a && b }

    private fun CaveLine.mergeWith(other: CaveLine): CaveLine = zip(other).map { (a, b) -> a || b }

    fun visualize() = tower.toSortedMap(compareByDescending { it }).map { line ->
        line.value.joinToString("") { if (it) "#" else "." }
    }.joinToString("\n")
}

enum class GasJet { LEFT, RIGHT }

data class DropCycle(val rockShapeIndex: Int, val gasJetIndex: Int, val lines: List<CaveLine>)
data class TowerState(val rocks: Long, val height: Long)
data class RockPattern(val rocksStart: Long, val rocksEnd: Long, val height: Long, val rockShapeIndex: Int, val gasJetIndex: Int)

sealed class RockShape {
    abstract val startRock: Rock

    data class HorizontalLine(override val startRock: Rock = listOf(
            listOf(false, false, true, true, true, true, false)
    )) : RockShape()

    data class PlusSign(override val startRock: Rock = listOf(
            listOf(false, false, false, true, false, false, false),
            listOf(false, false, true, true, true, false, false),
            listOf(false, false, false, true, false, false, false)
    )) : RockShape()

    data class MirroredL(override val startRock: Rock = listOf(
            listOf(false, false, true, true, true, false, false),
            listOf(false, false, false, false, true, false, false),
            listOf(false, false, false, false, true, false, false),
    )) : RockShape()

    data class VerticalLine(override val startRock: Rock = listOf(
            listOf(false, false, true, false, false, false, false),
            listOf(false, false, true, false, false, false, false),
            listOf(false, false, true, false, false, false, false),
            listOf(false, false, true, false, false, false, false)
    )) : RockShape()

    data class Rectangle(override val startRock: Rock = listOf(
            listOf(false, false, true, true, false, false, false),
            listOf(false, false, true, true, false, false, false),
    )) : RockShape()
}

fun String.toJetPattern(): List<GasJet> = this.map {
    when (it) {
        '<' -> LEFT
        '>' -> RIGHT
        else -> error("Unknown gas jet found: $it")
    }
}