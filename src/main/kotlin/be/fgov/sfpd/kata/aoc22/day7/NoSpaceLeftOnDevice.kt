package be.fgov.sfpd.kata.aoc22.day7

import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ChangeDirCommand
import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ListCommand
import be.fgov.sfpd.kata.aoc22.day7.FSElement.FSDirectory
import be.fgov.sfpd.kata.aoc22.day7.FSElement.FSFile

fun part1(input: String) =
        input.toFSCommands().let { commands ->
            DeviceFileSystem().browse(commands)
                    .rootDirectory
                    .allDirectories()
                    .filter { it.totalSize() <= 100000 }
                    .sumOf { it.totalSize() }
        }

class DeviceFileSystem {
    private var currentDir = FSDirectory(name = "/", null)
    val rootDirectory = currentDir

    fun browse(commands: List<FSCommand>): DeviceFileSystem {
        commands.forEach { command ->
            when (command) {
                is ChangeDirCommand -> currentDir = goTo(command.target)
                is ListCommand -> {
                    for (element in command.elements) {
                        currentDir.children.add(element.toChildren(parent = currentDir))
                    }
                }
            }
        }
        return this
    }

    fun totalSize() = rootDirectory.children.sumOf { it.totalSize() }

    private fun goTo(target: String): FSDirectory =
            when (target) {
                ".." -> currentDir.parent ?: error("cannot go past root")
                else -> currentDir.children.find { it.name == target } as? FSDirectory
                        ?: error(" $target is not a directory")
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

        fun allDirectories(): List<FSDirectory> {
            val allChildren = children.filterIsInstance<FSDirectory>().flatMap { it.allDirectories() }
            return if (name != "/") allChildren + this else allChildren
        }
    }

    class FSFile(name: String, parent: FSDirectory?, val size: Int) : FSElement(name, parent) {
        override fun totalSize() = size
    }
}

sealed class FSCommand {
    data class ChangeDirCommand(val target: String) : FSCommand()
    data class ListCommand(val elements: List<String>) : FSCommand()
}

fun String.toFSCommands(): List<FSCommand> {
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
