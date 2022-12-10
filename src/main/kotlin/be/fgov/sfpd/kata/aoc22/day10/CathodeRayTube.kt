package be.fgov.sfpd.kata.aoc22.day10

import be.fgov.sfpd.kata.aoc22.mapLines

val interestingCycles = listOf(20, 60, 100, 140, 180, 220)
fun part1(input: String) = VideoSystem().executeProgram(input.toCRTCommands()).history
        .filter { it.cycle in interestingCycles }
        .sumOf { it.cycle * it.registerX }




class VideoSystem() {
    var history: List<CPUState> = mutableListOf(CPUState())

    fun executeProgram(command: List<CRTCommand>): VideoSystem {
        command.forEach { this.execute(it) }
        return this
    }

    fun execute(command: CRTCommand) {
        history = when (command.name) {
            "noop" -> executeCycle(command)
            "addx" -> {
                history = executeCycle(CRTCommand("noop", 0))
                executeCycle(command)
            }
            else -> error("Invalid Command: ${command.name}")
        }
    }

    private fun executeCycle(command: CRTCommand) = history.plus(history.last().afterExecuting(command))
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