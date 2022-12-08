package be.fgov.sfpd.kata.aoc22.day7

import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ChangeDirCommand
import be.fgov.sfpd.kata.aoc22.day7.FSCommand.ListCommand
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NoSpaceLeftOnDeviceTest {

    @Test
    fun `browse ListCommand`() {
        val input = readFile("day7/exampleInput.txt")
        val actual = DeviceFileSystem()

        actual.browse(input.toFSCommands())

        assertThat(actual.totalSize()).isEqualTo(48381165)
    }


    @Test
    fun `parse to commands`() {
        val input = readFile("day7/exampleInput.txt")

        assertThat(input.toFSCommands()).isEqualTo(
                listOf(ChangeDirCommand("/"),
                        ListCommand(listOf("dir a", "14848514 b.txt", "8504156 c.dat", "dir d")),
                        ChangeDirCommand("a"),
                        ListCommand(listOf("dir e", "29116 f", "2557 g", "62596 h.lst")),
                        ChangeDirCommand("e"),
                        ListCommand(listOf("584 i")),
                        ChangeDirCommand(".."),
                        ChangeDirCommand(".."),
                        ChangeDirCommand("d"),
                        ListCommand(listOf("4060174 j", "8033020 d.log", "5626152 d.ext", "7214296 k"))
                ))
    }
}
