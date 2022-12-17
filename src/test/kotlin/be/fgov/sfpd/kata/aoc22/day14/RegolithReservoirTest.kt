package be.fgov.sfpd.kata.aoc22.day14

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day14.Filler.ROCK
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class RegolithReservoirTest {

    @Test
    fun `test toCave`() {
        assertThat("498,4 -> 498,6 -> 496,6".toCave().also { println(it.visualize()) }).isEqualTo(mapOf(
                Point(498, 4) to ROCK,
                Point(498, 5) to ROCK,
                Point(498, 6) to ROCK,
                Point(497, 6) to ROCK,
                Point(496, 6) to ROCK,
        ))
    }

    @Test
    fun `example input visualize start cave`() {
        val input = readFile("day14/exampleInput.txt")
        assertThat(input.toCave().visualize()).isEqualTo("""......+...
..........
..........
..........
....#...##
....#...#.
..###...#.
........#.
........#.
#########.""".trimIndent())
    }

    @Test
    fun `example input visualize after one sand added`() {
        val input = readFile("day14/exampleInput.txt")
        assertThat(input.toCave().addUnitOfSand().first.visualize().also { println(it) }).isEqualTo("""......+...
..........
..........
..........
....#...##
....#...#.
..###...#.
........#.
......o.#.
#########.""".trimIndent())
    }

    @Test
    fun `example input visualize after cave was filled`() {
        val input = readFile("day14/exampleInput.txt")
        assertThat(input.toCave().fillUp().visualize().also { println(it) }).isEqualTo(""".......+...
...........
.......o...
......ooo..
.....#ooo##
....o#ooo#.
...###ooo#.
.....oooo#.
..o.ooooo#.
.#########.
...........
o..........""".trimIndent())
    }

}


