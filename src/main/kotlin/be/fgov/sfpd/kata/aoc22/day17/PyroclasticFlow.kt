package be.fgov.sfpd.kata.aoc22.day17

import be.fgov.sfpd.kata.aoc22.day17.GasJet.LEFT
import be.fgov.sfpd.kata.aoc22.day17.GasJet.RIGHT
import be.fgov.sfpd.kata.aoc22.day17.RockShape.*
import java.time.LocalTime


fun part1(input: String) = Cave(input.toJetPattern()).dropRocks(2022).heightOfTower()

fun part2(input: String) = ""

data class Cave(val jetPattern: List<GasJet>, val width: Int = 7) {
    private var tower: MutableMap<Long, CaveLine> = mutableMapOf(0L to ((0 until width).map { true }.toList()))
    private val shapes = listOf(HorizontalLine(), PlusSign(), MirroredL(), VerticalLine(), Rectangle())

    fun dropRocks(numberOfRocks: Long): Cave {
        var gasJetIndex = 0
        var rockShapeIndex = 0
        (0 until numberOfRocks).forEach { rockNumber ->
            if (rockNumber % 100_000 == 0L) println("${"%,d".format(rockNumber)} were dropped... at ${LocalTime.now()}")
            gasJetIndex = dropRock(shapes[rockShapeIndex], gasJetIndex)
            rockShapeIndex = rockShapeIndex.nextOrFirstIndex(shapes.size)
            tower = tower.filterKeys { it > heightOfTower() - 30 }.toMutableMap()
        }
        return this
    }

    private fun dropRock(rock: RockShape, startGasJetIndex: Int): Int {
        var gasJetIndex = startGasJetIndex
        var currentHeight = heightOfTower() + 4
        var startRock = rock.caveLines
        var cameToRest = false
        while (!cameToRest) {
            val gasJet = jetPattern[gasJetIndex]
            val movingRock = startRock.hitWithGasJet(gasJet, currentHeight)

            if (movingRock.overlapsTower(tower, currentHeight - 1)) {
                addRockInRest(movingRock, currentHeight)
                cameToRest = true
            } else {
                currentHeight -= 1
                gasJetIndex = gasJetIndex.nextOrFirstIndex(jetPattern.size)
                startRock = movingRock
            }

        }

        return gasJetIndex.nextOrFirstIndex(jetPattern.size)
    }

    private fun addRockInRest(movingRock: Rock, currentHeight: Long) {
        movingRock.forEachIndexed { index, rockLine ->
            tower[currentHeight + index] = tower[currentHeight + index]?.let {
                it.zip(rockLine).map { (tower, rock) ->
                    if (rock) true else tower
                }
            } ?: rockLine
        }
    }

    private fun Rock.hitWithGasJet(gasJet: GasJet, currentHeight: Long): Rock {
        val moveToTry = when (gasJet) {
            LEFT -> this.moveLeftIfNoWall()
            RIGHT -> this.moveRightIfNoWall()
        }
        return if (moveToTry.overlapsTower(tower, currentHeight)) this else moveToTry
    }

    fun visualize() =
            tower.toSortedMap(compareByDescending { it }).map { line ->
                line.value.joinToString("") { if (it) "#" else "." }
            }.joinToString("\n")

    fun heightOfTower() = tower.maxBy { it.key }.key
}


private fun Rock.overlapsTower(topLines: Map<Long, CaveLine>, bottomAt: Long): Boolean {
    val towerLinesToCheck = (bottomAt..this.size + bottomAt).mapNotNull { topLines[it] }
    return towerLinesToCheck.zip(this).any { (rockLine, towerLine) ->
        rockLine overlaps towerLine
    }
}

private infix fun CaveLine.overlaps(towerLine: CaveLine) =
        zip(towerLine).any { (rock, tower) ->
            rock && tower
        }


private fun Int.nextOrFirstIndex(total: Int): Int = if (this + 1 >= total) 0 else this + 1
private fun Rock.moveLeftIfNoWall() = if (map { it.first() }.any { it }) this else map { it.shiftLeft() }
private fun Rock.moveRightIfNoWall() = if (map { it.last() }.any { it }) this else map { it.shiftRight() }
private fun CaveLine.shiftLeft() = this.drop(1) + false
private fun CaveLine.shiftRight() = listOf(false) + this.dropLast(1)

typealias CaveLine = List<Boolean>
typealias Rock = List<CaveLine>

enum class GasJet { LEFT, RIGHT }

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