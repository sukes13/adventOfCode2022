package be.fgov.sfpd.kata.aoc22.day19

import be.fgov.sfpd.kata.aoc22.day19.MineRobot.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NotEnoughMineralsTest {

    @Test
    fun `parse input`() {
        val input = readFile("day19/exampleInput.txt")

        assertThat(input.toBlueprints()).isEqualTo(listOf(
                Blueprint(1, listOf(OreRobot(4), ClayRobot(2), ObsidianRobot(3, 14), GeodeRobot(2, 7))),
                Blueprint(2, listOf(OreRobot(2), ClayRobot(3), ObsidianRobot(3, 8), GeodeRobot(3, 12))),
        ))
    }
}
