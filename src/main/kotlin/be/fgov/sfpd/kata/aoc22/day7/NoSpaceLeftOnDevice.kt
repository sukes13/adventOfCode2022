package be.fgov.sfpd.kata.aoc22.day7

import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ChangeDirCommand
import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ListCommand
import be.fgov.sfpd.kata.aoc22.day7.FSElement.FSDirectory
import be.fgov.sfpd.kata.aoc22.day7.FSElement.FSFile

fun part1(input: String) =
        input.toFSCommands().let { commands ->
            DeviceFileSystem().browse(commands).rootDirectory
                    .allDirectorySizes().filter { it <= 100000 }.sum()
        }

fun part2(input: String): Int {
    val rootDirectory = input.toFSCommands().let { DeviceFileSystem().browse(it).rootDirectory }
    val emptySpace = 70000000 - rootDirectory.totalSize()
    val spaceToFind = 30000000 - emptySpace

    return if (spaceToFind > 0) {
        rootDirectory.allDirectorySizes().filter { it >= spaceToFind }.min()
    } else 0
}

class DeviceFileSystem {
    private var currentDir = FSDirectory(name = "/", null)
    val rootDirectory = currentDir

    fun browse(commands: List<FSCommand>): DeviceFileSystem {
        commands.forEach { command ->
            when (command) {
                is ChangeDirCommand -> currentDir = goTo(command.target)
                is ListCommand -> command.elements.forEach {
                    currentDir.children += it.toChildren(parent = currentDir)
                }
            }
        }
        return this
    }

    private fun goTo(target: String): FSDirectory =
            when (target) {
                ".." -> currentDir.parent ?: error("cannot go past root")
                else -> currentDir.children.find { it.name == target } as? FSDirectory ?: error(" $target is not a directory")
            }

    private fun String.toChildren(parent: FSDirectory): FSElement =
            when {
                this.startsWith("dir") -> FSDirectory(name = this.drop(4), parent = parent)
                else -> this.split(" ").let { (size, name) -> FSFile(name = name, parent = parent, size = size.toInt()) }
            }
}


sealed class FSElement(val name: String, val parent: FSDirectory?) {
    abstract fun totalSize(): Int

    class FSDirectory(name: String, parent: FSDirectory?, val children: MutableList<FSElement> = mutableListOf()) : FSElement(name, parent) {
        override fun totalSize() = children.sumOf { it.totalSize() }

        fun allDirectorySizes(): List<Int> =
                children.filterIsInstance<FSDirectory>()
                        .flatMap { it.allDirectorySizes() }
                        .let { dirSize -> dirSize + this.totalSize() }
    }

    class FSFile(name: String, parent: FSDirectory, val size: Int) : FSElement(name, parent) {
        override fun totalSize() = size
    }
}

sealed class FSCommand {
    data class ChangeDirCommand(val target: String) : FSCommand()
    data class ListCommand(val elements: List<String>) : FSCommand()
}

//parsing...
internal fun String.toFSCommands(): List<FSCommand> {
    val lines = lines()
    return lines.mapIndexedNotNull { index, line ->
        when {
            line.startsWith("$ cd") -> ChangeDirCommand(line.drop(4).trim())
            line.startsWith("$ ls") -> ListCommand(lines.readUntilNextCommandFrom(index))
            else -> null
        }
    }.drop(1)
}

private fun List<String>.readUntilNextCommandFrom(index: Int) = drop(index + 1).takeWhile { !it.contains('$') }

