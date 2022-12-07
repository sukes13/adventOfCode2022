package be.fgov.sfpd.kata.aoc22.day7

import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ChangeDirCommand
import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ListCommand


fun String.toFSCommands(): List<FSCommand> {
    val lines = lines()
    return lines.mapIndexedNotNull { index, line ->
        when {
            line.startsWith("$ cd") -> ChangeDirCommand(line.drop(4).trim())
            line.startsWith("$ ls") -> ListCommand(readUntilNextCommand(lines, index))
            else -> null
        }
    }
}

private fun readUntilNextCommand(lines: List<String>, index: Int) = lines.drop(index + 1).takeWhile { !it.contains('$') }

sealed class FSCommand {
    data class ChangeDirCommand(val target: String) : FSCommand()
    data class ListCommand(val elements: List<String>) : FSCommand()
}
