package be.fgov.sfpd.kata.aoc22.day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CathodeRayTubeTest {
    @Test
    fun `show example input`() {

    }

    @Test
    fun `test toCRTCommands`() {
        assertThat("""noop
addx 3
addx -5""".trimIndent().toCRTCommands()).isEqualTo(listOf(CRTCommand("noop"), CRTCommand("addx", 3), CRTCommand("addx", -5)))
    }
}

