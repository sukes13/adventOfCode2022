package be.fgov.sfpd.kata.aoc22.day23

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day23.Direction.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class UnstableDiffusionTest {

    @Test
    fun `parse input to SowingElves`() {
        val elves = readFile("day23/exampleInputSmall.txt").toElves()
        assertThat(elves).contains(Point(2, 1), Point(3, 1))
    }

    @Test
    fun `test considerPositions`() {
        val elves = readFile("day23/exampleInputSmall.txt").toElves().considerPositionsStartingFrom(NORTH)
        assertThat(elves).containsAllEntriesOf(listOf( Point(2, 1) to Point(2, 0), Point(3, 1) to Point(3, 0)).toMap())
    }

    @Test
    fun `test visualize`() {
        val elves = readFile("day23/exampleInputSmall.txt").toElves()

        assertThat(elves.visualize()).isEqualTo("""##
#.
..
##""")
    }

    @Test
    fun `test first round small`() {
        val elves = readFile("day23/exampleInputSmall.txt").toElves().moveIfPossibleOrNullFrom(NORTH)

        assertThat(elves?.visualize()).isEqualTo("""##
..
#.
.#
#.""")
    }

    @Test
    fun `test spreadOut small`() {
        val elves = readFile("day23/exampleInputSmall.txt").toElves().spreadOut(10)

        assertThat(elves.first.visualize()).isEqualTo("""..#..
....#
#....
....#
.....
..#..""")
    }

    @Test
    fun `test first round bigger`() {
        val elves = readFile("day23/exampleInput.txt").toElves().moveIfPossibleOrNullFrom(NORTH)

        assertThat(elves?.visualize()).isEqualTo(""".....#...
...#...#.
.#..#.#..
.....#..#
..#.#.##.
#..#.#...
#.#.#.##.
.........
..#..#...""")
    }

    @Test
    fun `test spreadOut bigger`() {
        val elves = readFile("day23/exampleInput.txt").toElves().spreadOut(10)

        assertThat(elves.first.visualize()).isEqualTo("""......#.....
..........#.
.#.#..#.....
.....#......
..#.....#..#
#......##...
....##......
.#........#.
...#.#..#...
............
...#..#..#..""")
    }

    @ParameterizedTest(name = "Direction:  \"{0}\", startingFrom: \"{1}\"")
    @MethodSource("testDirectionNext")
    fun `test startFrom resorting`(start: Direction, result: List<Direction>) {
        assertThat(start.allDirectionsFrom()).containsExactly(*result.toTypedArray())
    }

    companion object {
        @JvmStatic
        fun testDirectionNext() = listOf(
                Arguments.of(NORTH, listOf(NORTH,SOUTH,WEST,EAST),
                Arguments.of(SOUTH, listOf(SOUTH,WEST,EAST,NORTH)),
                Arguments.of(WEST, listOf(WEST,EAST,NORTH,SOUTH)),
                Arguments.of(EAST, listOf(EAST,NORTH,SOUTH,WEST))),
        )
    }
}



