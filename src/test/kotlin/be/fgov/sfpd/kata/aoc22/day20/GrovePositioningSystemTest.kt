package be.fgov.sfpd.kata.aoc22.day20

import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GrovePositioningSystemTest {
    @Test
    fun `parse toGPSFile`() {
        val input = readFile("day20/exampleInput.txt")
        assertThat(input.toGPSFile()).isEqualTo(listOf(
                GPSNumber(0, 1),
                GPSNumber(1, 2),
                GPSNumber(2, -3),
                GPSNumber(3, 3),
                GPSNumber(4, -2),
                GPSNumber(5, 0),
                GPSNumber(6, 4)))
    }

    @Test
    fun `decode GPSFile`() {
        val input = readFile("day20/exampleInput.txt")
        assertThat(input.toGPSFile().decode()).isEqualTo(listOf(
                GPSNumber(4, -2),
                GPSNumber(0, 1),
                GPSNumber(1, 2),
                GPSNumber(2, -3),
                GPSNumber(6, 4),
                GPSNumber(5, 0),
                GPSNumber(3, 3)))
    }
}



