package be.fgov.sfpd.kata.aoc22.day5

import be.fgov.sfpd.kata.aoc22.readFile
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class SupplyStacksTest {

    @Test
    fun `test input toCargo`() {
        val (cargoInput, _) = readFile("day5/exampleInput.txt").splitOnEmptyLine()

        val cargo = mapOf(0 to listOf("N", "Z"), 1 to listOf("D", "C", "M"), 2 to listOf("P"))

        assertThat(cargoInput.toCargo()).isEqualTo(cargo)
    }

    @Test
    fun `test input toCrateMoves`() {
        val (_, moveInput) = readFile("day5/exampleInput.txt").splitOnEmptyLine()

        val moves = listOf(CrateMove(1, 2, 1), CrateMove(3, 1, 3), CrateMove(2, 2, 1), CrateMove(1, 1, 2))

        assertThat(moveInput.toCrateMoves()).isEqualTo(moves)
    }

    @Test
    fun `test getTopCrates`() {
        val cargo = mapOf(0 to listOf("D", "N", "Z"), 1 to listOf("C", "M"), 2 to listOf("P"))

        assertThat(cargo.getTopCrates()).isEqualTo("DCP")
    }

    @ParameterizedTest(name = "CrateMove:  \"{0}\" of cargo: \"{1}\" results in cargo: \"{2}\"")
    @MethodSource("testCargoMoves")
    fun `test move crate`(crateMove: CrateMove, startCargo: Cargo, expectedCargo: Cargo) {
        val actual = startCargo.execute(crateMove) { it.reversed() }
        assertThat(actual).isEqualTo(expectedCargo)
    }

    companion object {
        @JvmStatic
        fun testCargoMoves() = listOf(
                Arguments.of(CrateMove(1, 2, 1),
                        mapOf(0 to listOf("N", "Z"), 1 to listOf("D", "C", "M"), 2 to listOf("P")),
                        mapOf(0 to listOf("D", "N", "Z"), 1 to listOf("C", "M"), 2 to listOf("P"))),
                Arguments.of(CrateMove(3, 1, 3),
                        mapOf(0 to listOf("D", "N", "Z"), 1 to listOf("C", "M"), 2 to listOf("P")),
                        mapOf(0 to listOf(), 1 to listOf("C", "M"), 2 to listOf("Z", "N", "D", "P"))),
                Arguments.of(CrateMove(2, 2, 1),
                        mapOf(0 to listOf(), 1 to listOf("C", "M"), 2 to listOf("Z", "N", "D", "P")),
                        mapOf(0 to listOf("M", "C"), 1 to listOf(), 2 to listOf("Z", "N", "D", "P"))),
                Arguments.of(CrateMove(1, 1, 2),
                        mapOf(0 to listOf("M", "C"), 1 to listOf(), 2 to listOf("Z", "N", "D", "P")),
                        mapOf(0 to listOf("C"), 1 to listOf("M"), 2 to listOf("Z", "N", "D", "P"))),
        )
    }

    @Test
    fun `test visualize cargo`() {
        val cargo = mapOf(0 to listOf("N", "Z"), 1 to listOf("D", "C", "M"), 2 to listOf("P"))

        assertThat(cargo.visualize().also { println(it) })
                .isEqualTo("""
    [D]     
[N] [C]     
[Z] [M] [P]
""".trimEnd())
    }
}




