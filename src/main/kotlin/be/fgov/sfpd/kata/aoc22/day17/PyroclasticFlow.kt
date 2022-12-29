package be.fgov.sfpd.kata.aoc22.day17

import be.fgov.sfpd.kata.aoc22.day17.GasJet.LEFT
import be.fgov.sfpd.kata.aoc22.day17.GasJet.RIGHT
import be.fgov.sfpd.kata.aoc22.day17.RockShape.*


fun part1(input: String) = Cave(input.toJetPattern()).dropRocks(2022).heightOfTower()

fun part2(input: String) = Cave(input.toJetPattern()).dropALotOfRocks(1000000000000L)

data class Cave(val jetPattern: List<GasJet>, val width: Int = 7) {
    private var tower: MutableMap<Long, CaveLine> = mutableMapOf(0L to ((0 until width).map { true }.toList()))
    private val shapes = listOf(HorizontalLine(), PlusSign(), MirroredL(), VerticalLine(), Rectangle())

    fun dropALotOfRocks(numberOfRocks: Long): Long {
        val patternEnd = searchPattern(numberOfRocks)
        val rocksInPattern = patternEnd.rocksEnd - patternEnd.rocksStart
        val patternRepeats = (numberOfRocks - patternEnd.rocksStart) / rocksInPattern
        val rocksInRepeats = patternRepeats * rocksInPattern
        val remainingRocks = numberOfRocks - patternEnd.rocksStart - rocksInRepeats

        val heightOfPattern = patternEnd.heightEnd - patternEnd.heightStart
        dropRocks(remainingRocks, patternEnd.gasJetIndex.nextOrFirstIndex(jetPattern.size), patternEnd.rockShapeIndex.nextOrFirstIndex(shapes.size))

        return (heightOfPattern * (patternRepeats - 1)) + heightOfTower()
    }


    private fun searchPattern(numberOfRocks: Long): PatternStart {
        var gasJetIndex = 0
        var rockShapeIndex = 0
        val dropCycles = mutableMapOf<DropCycle, TowerState>()
        (1..numberOfRocks).forEach { rockNumber ->
            if (tower.size > 100) {
                tower = tower.filterKeys { it > heightOfTower() - 30 }.toMutableMap()
            }
            gasJetIndex = dropRock(rockShapeIndex, gasJetIndex)

            val dropCycle = DropCycle(rockShapeIndex, gasJetIndex, tower.filterKeys { it > heightOfTower() - 10 }.values.toList())
            val match = dropCycles[dropCycle]
            if (match != null) {
                println("PATTERN detected? $dropCycle")
                return PatternStart(match.rockNumber, rockNumber, match.heightOfTower, heightOfTower(), rockShapeIndex, gasJetIndex)
            } else {
                dropCycles[dropCycle] = TowerState(rockNumber, heightOfTower())
            }

            rockShapeIndex = rockShapeIndex.nextOrFirstIndex(shapes.size)
            gasJetIndex = gasJetIndex.nextOrFirstIndex(jetPattern.size)

        }
        error("No pattern detected...")
    }

    fun dropRocks(numberOfRocks: Long, gasJet: Int = 0, rockShape: Int = 0): Cave {
        var gasJetIndex = gasJet
        var rockShapeIndex = rockShape

        (0 until numberOfRocks).forEach { _ ->
            gasJetIndex = dropRock(rockShapeIndex, gasJetIndex)

            rockShapeIndex = rockShapeIndex.nextOrFirstIndex(shapes.size)
            gasJetIndex = gasJetIndex.nextOrFirstIndex(jetPattern.size)
        }
        return this
    }

    private fun dropRock(rockShapeIndex: Int, startGasJetIndex: Int): Int {
        var gasJetIndex = startGasJetIndex
        var currentHeight = heightOfTower() + 4
        var movingRock = shapes[rockShapeIndex].caveLines
        var cameToRest = false
        while (!cameToRest) {
            val blownRock = movingRock.hitWithGasJet(jetPattern[gasJetIndex]).let {
                if (it.overlapsTower(tower, currentHeight)) movingRock else it
            }

            if (currentHeight <= heightOfTower() + 1 && blownRock.overlapsTower(tower, currentHeight - 1)) {
                addRockInRest(blownRock, currentHeight)
                cameToRest = true
            } else {
                currentHeight -= 1
                gasJetIndex = gasJetIndex.nextOrFirstIndex(jetPattern.size)
                movingRock = blownRock
            }
        }

        return gasJetIndex
    }

    private fun addRockInRest(movingRock: Rock, currentHeight: Long) {
        movingRock.forEachIndexed { index, rockLine ->
            tower[currentHeight + index] = tower[currentHeight + index]?.mergeWith(rockLine) ?: rockLine
        }
    }

    private fun Rock.hitWithGasJet(gasJet: GasJet) = when (gasJet) {
        LEFT -> if (map { it.first() }.any { it }) this else map { it.shiftLeft() }
        RIGHT -> if (map { it.last() }.any { it }) this else map { it.shiftRight() }
    }

    fun heightOfTower() = tower.maxBy { it.key }.key

    fun visualize() = tower.toSortedMap(compareByDescending { it }).map { line ->
        line.value.joinToString("") { if (it) "#" else "." }
    }.joinToString("\n")


    private fun Rock.overlapsTower(tower: Map<Long, CaveLine>, rockBottomAt: Long): Boolean {
        val towerLinesToCheck = (rockBottomAt..rockBottomAt + this.size).mapNotNull { tower[it] }
        return towerLinesToCheck.zip(this).any { (rockLine, towerLine) ->
            rockLine overlaps towerLine
        }
    }

    private infix fun CaveLine.overlaps(towerLine: CaveLine) = zip(towerLine).any { (rock, tower) -> rock && tower }

    private fun CaveLine.mergeWith(rockLine: CaveLine) = zip(rockLine).map { (tower, rock) -> if (rock) true else tower }

    private fun CaveLine.shiftLeft() = this.drop(1) + false

    private fun CaveLine.shiftRight() = listOf(false) + this.dropLast(1)

    private fun Int.nextOrFirstIndex(total: Int): Int = if (this + 1 >= total) 0 else this + 1
}


typealias CaveLine = List<Boolean>
typealias Rock = List<CaveLine>

enum class GasJet { LEFT, RIGHT }

data class DropCycle(val rockShapeIndex: Int, val gasJetIndex: Int, val lines: List<CaveLine>)
data class PatternStart(val rocksStart: Long, val rocksEnd: Long, val heightStart: Long, val heightEnd: Long, val rockShapeIndex: Int, val gasJetIndex: Int)
data class TowerState(val rockNumber: Long, val heightOfTower: Long)

sealed class RockShape {
    abstract val caveLines: Rock

    data class HorizontalLine(override val caveLines: Rock = listOf(
            listOf(false, false, true, true, true, true, false)
    )) : RockShape()

    data class PlusSign(override val caveLines: Rock = listOf(
            listOf(false, false, false, true, false, false, false),
            listOf(false, false, true, true, true, false, false),
            listOf(false, false, false, true, false, false, false)
    )) : RockShape()

    data class MirroredL(override val caveLines: Rock = listOf(
            listOf(false, false, true, true, true, false, false),
            listOf(false, false, false, false, true, false, false),
            listOf(false, false, false, false, true, false, false),
    )) : RockShape()

    data class VerticalLine(override val caveLines: Rock = listOf(
            listOf(false, false, true, false, false, false, false),
            listOf(false, false, true, false, false, false, false),
            listOf(false, false, true, false, false, false, false),
            listOf(false, false, true, false, false, false, false)
    )) : RockShape()

    data class Rectangle(override val caveLines: Rock = listOf(
            listOf(false, false, true, true, false, false, false),
            listOf(false, false, true, true, false, false, false),
    )) : RockShape()
}

fun String.toJetPattern(): List<GasJet> =
        this.map {
            when (it) {
                '<' -> LEFT
                '>' -> RIGHT
                else -> error("Unknown gas jet found: $it")
            }
        }