package be.fgov.sfpd.kata.aoc22.day5

import be.fgov.sfpd.kata.aoc22.readFile
import be.fgov.sfpd.kata.aoc22.spitOnEmptyLine
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class SupplyStacksTest {

    @Test
    fun `test input toCargo`() {
        val (cargoInput, _) = readFile("day5/exampleInput.txt").spitOnEmptyLine()

        val cargo = mapOf(0 to listOf("N","Z"),1 to listOf("D","C","M"),2 to listOf("P"))

        assertThat(cargoInput.toCargo()).isEqualTo(cargo)
    }

    @Test
    fun `test input parsing`() {
        val (_, moveInput) = readFile("day5/exampleInput.txt").spitOnEmptyLine()

        val moves = listOf(CrateMove(1,2,1),CrateMove(3,1,3),CrateMove(2,2,1),CrateMove(1,1,2))

        assertThat(moveInput.toCrateMoves()).isEqualTo(moves)
    }

    @ParameterizedTest(name = "Sweeps:  \"{0}\" overlap at: \"{1}\"")
    @MethodSource("testOverlaps")
    fun `test input - find overlap`(input: String, result: Set<Int>) {
        assertThat(true).isEqualTo(true)
    }

    companion object {
        @JvmStatic
        fun testOverlaps() = listOf(
                Arguments.of("2-4,6-8", setOf<Int>()),
                Arguments.of("2-3,4-5", setOf<Int>()),
                Arguments.of("5-7,7-9", setOf(7)),
                Arguments.of("2-8,3-7", (3..7).toSet()),
                Arguments.of("6-6,4-6", setOf(6)),
                Arguments.of("2-6,4-8", (4..6).toSet()),
        )
    }

}



