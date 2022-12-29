package be.fgov.sfpd.kata.aoc22.day17

import be.fgov.sfpd.kata.aoc22.day17.RockShape.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class PyroclasticFlowTest {

    @ParameterizedTest(name = "Drop \"{0}\" rock(s)")
    @MethodSource("testDrops")
    fun `test dropRocks for example input`(numberOfRocks : Long, result : String) {
        val input = readFile("day17/exampleInput.txt")
        val cave = Cave(input.toJetPattern())

        cave.dropRocks(numberOfRocks,0,0)

        assertThat(cave.visualize()).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun testDrops() = listOf(
                Arguments.of(1L,    """..####.
#######"""),
                Arguments.of(2L,    """...#...
..###..
...#...
..####.
#######"""),
                Arguments.of(3L,    """..#....
..#....
####...
..###..
...#...
..####.
#######"""),
                Arguments.of(5L,    """....##.
....##.
....#..
..#.#..
..#.#..
#####..
..###..
...#...
..####.
#######"""),
                Arguments.of(10L,    """....#..
....#..
....##.
##..##.
######.
.###...
..#....
.####..
....##.
....##.
....#..
..#.#..
..#.#..
#####..
..###..
...#...
..####.
#######"""),
        )
    }



}
