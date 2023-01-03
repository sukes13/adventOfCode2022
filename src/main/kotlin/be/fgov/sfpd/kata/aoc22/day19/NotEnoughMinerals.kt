package be.fgov.sfpd.kata.aoc22.day19

import be.fgov.sfpd.kata.aoc22.day19.MineRobot.*
import be.fgov.sfpd.kata.aoc22.mapLines


fun part1(input: String) = input.toBlueprints().sumOf { it.id * it.maxGeodes(24) }

fun part2(input: String) = input.toBlueprints().first().maxGeodes(32)

private val startState = MineState(0, listOf(0, 0, 0, 0), listOf(1, 0, 0, 0))

data class Blueprint(val id: Int, val robots: List<MineRobot>) {
    fun maxGeodes(minutes: Int): Int {
        val maxNeeded = listOf(
                robots.maxOf { it.ore },
                robots.filterIsInstance<ObsidianRobot>().single().clay,
                robots.filterIsInstance<GeodeRobot>().single().obsidian,
                Int.MAX_VALUE
        )
        return listOf(startState).findMaximumGeodes(minutes, robots, maxNeeded)
    }
}

private fun List<MineState>.findMaximumGeodes(minutes: Int, robotTypes: List<MineRobot>, maxNeeded: List<Int>): Int {
    val queue: MutableSet<MineState> = this.toMutableSet()
    var maxGeodes = 0

    while (queue.isNotEmpty()) {
        val current = queue.first()
        queue.remove(current)
        if (current.minute < minutes) {
            robotTypes.mapIndexed { index, type ->
                if (type.buildPossibleFor(current.robotsNumbers) && current.robotsNumbers[index] < maxNeeded[index]) {
                    current.nextStateFor(type, minutes)
                } else null
            }.filterNotNull().also {
                queue.addAll(it)
            }
        } else {
            maxGeodes = maxOf(maxGeodes, current.minerals.last())
        }
    }

    return maxGeodes
}


data class MineState(val minute: Int, val minerals: List<Int>, val robotsNumbers: List<Int>) {

    fun nextStateFor(robotType: MineRobot, minutes: Int): MineState {
        val waitTime = minutesToWaitFor(robotType) + 1
        return if (minute + waitTime < minutes) {
            copy(
                    minute = minute + waitTime,
                    minerals = mineDuring(waitTime).payForBuildOf(robotType),
                    robotsNumbers = buildRobot(robotType)
            )
        } else copy(
                minute = minutes,
                minerals = mineDuring(minutes - minute),
        )
    }

    private fun minutesToWaitFor(robotType: MineRobot) = robotType.cost.mapIndexed { index, cost ->
        if (cost > 0) {
            val needed = cost - minerals[index]
            if (needed > 0) {
                (needed / robotsNumbers[index]) + if (needed % robotsNumbers[index] == 0) 0 else 1
            } else 0
        } else 0
    }.max()

    private fun List<Int>.payForBuildOf(robotType: MineRobot) = zip(robotType.cost) { mineral, cost -> mineral - cost }

    private fun mineDuring(minutes: Int) = minerals.mapIndexed { index, mineral ->
        mineral + robotsNumbers[index] * minutes
    }

    private fun buildRobot(robotType: MineRobot) = robotsNumbers.mapIndexed { index, robot ->
        if (index == robotType.inventoryIndex) robot + 1 else robot
    }
}


sealed class MineRobot(val inventoryIndex: Int) {
    abstract val ore: Int
    abstract val cost: List<Int>
    abstract fun canBuildIf(current: List<Int>): Boolean
    abstract fun buildPossibleFor(current: List<Int>): Boolean

    data class OreRobot(override val ore: Int) : MineRobot(0) {
        override val cost: List<Int> = listOf(ore, 0, 0, 0)
        override fun canBuildIf(current: List<Int>) = current[0] >= ore
        override fun buildPossibleFor(current: List<Int>) = current[0] > 0
    }

    data class ClayRobot(override val ore: Int) : MineRobot(1) {
        override val cost: List<Int> = listOf(ore, 0, 0, 0)
        override fun canBuildIf(current: List<Int>) = current[0] >= ore
        override fun buildPossibleFor(current: List<Int>) = current[0] > 0
    }

    data class ObsidianRobot(override val ore: Int, val clay: Int) : MineRobot(2) {
        override val cost: List<Int> = listOf(ore, clay, 0, 0)
        override fun canBuildIf(current: List<Int>) = current[0] >= ore && current[1] >= clay
        override fun buildPossibleFor(current: List<Int>) = current[0] > 0 && current[1] > 0
    }

    data class GeodeRobot(override val ore: Int, val obsidian: Int) : MineRobot(3) {
        override val cost: List<Int> = listOf(ore, 0, obsidian, 0)
        override fun canBuildIf(current: List<Int>) = current[0] >= ore && current[2] >= obsidian
        override fun buildPossibleFor(current: List<Int>) = current[0] > 0 && current[2] > 0
    }
}

fun String.toBlueprints(): List<Blueprint> = mapLines {
    Blueprint(
            id = it.substringAfter("Blueprint ").substringBefore(": Each ore").toInt(),
            robots = listOf(
                    OreRobot(ore = it.substringAfter("Each ore robot costs ").substringBefore(" ore. Each clay").toInt()),
                    ClayRobot(ore = it.substringAfter("Each clay robot costs ").substringBefore(" ore. Each obsidian").toInt()),
                    ObsidianRobot(
                            ore = it.substringAfter("Each obsidian robot costs ").substringBefore(" ore and ").toInt(),
                            clay = it.substringAfter(" ore and ").substringBefore(" clay. Each geode").toInt()
                    ),
                    GeodeRobot(
                            ore = it.substringAfter("Each clay robot costs ").substringBefore(" ore. Each obsidian").toInt(),
                            obsidian = it.substringAfterLast(" ore and ").substringBefore(" obsidian.").toInt()
                    ),
            )
    )
}