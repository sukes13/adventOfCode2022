package be.fgov.sfpd.kata.aoc22.day10

import be.fgov.sfpd.kata.aoc22.mapLines

val interestingCycles = listOf(20, 60, 100, 140, 180, 220)
fun part1(input: String) = VideoSystem().executeProgram(input.toCRTCommands()).history
        .filter { it.cycle in interestingCycles }
        .sumOf { it.signalStrength }

fun part2(input: String) = VideoSystem().executeProgram(input.toCRTCommands()).image(rolloverAt = 40)

private class VideoSystem {
    val history = mutableListOf(CPUState())

    fun executeProgram(command: List<CRTCommand>): VideoSystem {
        command.forEach { this.execute(it) }
        return this
    }

    fun image(rolloverAt: Int) = history.dropLast(1).mapIndexed { index, cpuState ->
        when (cpuState.registerX) {
            in sprite(cpuState.cycle - 1, rolloverAt) -> "#"
            else -> "."
        }.let { pixel -> if ((index + 1) % rolloverAt == 0) pixel + "\n" else pixel }
    }.joinToString("").trimIndent()

    private fun execute(command: CRTCommand) {
        when (command.name) {
            "noop" -> history.add(cpuStateAfter(command))
            "addx" -> {
                history.add(cpuStateAfter(CRTCommand("noop")))
                history.add(cpuStateAfter(command))
            }
            else -> error("Invalid Command: ${command.name}")
        }
    }

    private fun cpuStateAfter(command: CRTCommand) = history.last().afterExecuting(command)

    private fun sprite(cycle: Int, rolloverAt: Int) = (cycle - 1..cycle + 1).map { it - (cycle / rolloverAt) * rolloverAt }
}

data class CPUState(val cycle: Int = 1, val registerX: Int = 1) {
    val signalStrength = cycle * registerX
    fun afterExecuting(crtCommand: CRTCommand) = copy(cycle = cycle + 1, registerX = registerX + crtCommand.value)
}

data class CRTCommand(val name: String, val value: Int = 0)

//parsing...
internal fun String.toCRTCommands() = mapLines { line ->
    when {
        line.trim().startsWith("noop") -> CRTCommand("noop")
        else -> line.split(" ").let { (command, value) -> CRTCommand(command, value.toInt()) }
    }
}

