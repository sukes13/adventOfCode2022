package be.fgov.sfpd.kata.aoc22.day10

import be.fgov.sfpd.kata.aoc22.mapLines

val interestingCycles = listOf(20, 60, 100, 140, 180, 220)
fun part1(input: String) = VideoSystem().executeProgram(input.toCRTCommands()).history
        .filter { it.cycle in interestingCycles }
        .sumOf { it.cycle * it.registerX }

fun part2(input: String) =
        VideoSystem().executeProgram(input.toCRTCommands())
                .lineImage(40).mapIndexed { index, cycleImage ->
                    if ((index + 1) % 40 == 0) cycleImage + "\n" else cycleImage
                }.joinToString("").trimIndent()

class VideoSystem() {
    val history = mutableListOf(CPUState())

    fun executeProgram(command: List<CRTCommand>): VideoSystem {
        command.forEach { this.execute(it) }
        return this
    }

    private fun execute(command: CRTCommand) {
        when (command.name) {
            "noop" -> history.add(cpuStateAfter(command))
            "addx" -> {
                history.add(cpuStateAfter(CRTCommand("noop", 0)))
                history.add(cpuStateAfter(command))
            }
            else -> error("Invalid Command: ${command.name}")
        }
    }

    fun lineImage(rolloverAt: Int) =
            history.dropLast(1).map { cpuState ->
                when (cpuState.registerX) {
                    in sprite(cpuState.cycle - 1, rolloverAt) -> "#"
                    else -> "."
                }
            }

    private fun cpuStateAfter(command: CRTCommand) = history.last().afterExecuting(command)

    private fun sprite(cycle: Int, rolloverAt: Int): List<Int> {
        val rollovers = (cycle / rolloverAt) * rolloverAt
        return listOf((if (cycle - 1 >= 0) cycle - 1 else 0), cycle, cycle + 1).map { it - rollovers }
    }
}

data class CPUState(val cycle: Int = 1, val registerX: Int = 1) {
    fun afterExecuting(crtCommand: CRTCommand) = this.copy(cycle = cycle + 1, registerX = registerX + crtCommand.value)
}

fun String.toCRTCommands() = mapLines { line ->
    when {
        line.trim().startsWith("noop") -> CRTCommand("noop")
        else -> line.split(" ").let { (command, value) -> CRTCommand(command, value.toInt()) }
    }
}


data class CRTCommand(val name: String, val value: Int = 0)