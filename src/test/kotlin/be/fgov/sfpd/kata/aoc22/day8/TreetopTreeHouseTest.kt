package be.fgov.sfpd.kata.aoc22.day8

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.day8.ViewDirection.*
import be.fgov.sfpd.kata.aoc22.readFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class TreetopTreeHouseTest {

    @ParameterizedTest(name = "Tree at:  \"{0}\" can see: \"{1}\"")
    @MethodSource("testTrees")
    fun `test create view map`(point: Point, lineOfSightMap: LineOfSightMap) {
        val treetopGrid = readFile("day8/exampleInput.txt").toTreetopGrid()
        assertThat(treetopGrid.linesOfSightOf(point)).containsAllEntriesOf(lineOfSightMap)
    }

    @ParameterizedTest(name = "Tree at:  \"{0}\" has scene score of: \"{1}\"")
    @MethodSource("testDistanceToEdge")
    fun `test distanceToEdge`(point: Point, direction: ViewDirection, score: Int) {
        val treetopGrid = readFile("day8/exampleInput.txt").toTreetopGrid()
        assertThat(point.distanceToEdge(direction, treetopGrid.size)).isEqualTo(score)
    }

    @ParameterizedTest(name = "Tree at:  \"{0}\" is visible: \"{1}\"")
    @MethodSource("testTreesLineOfSights")
    fun `test if point visible`(point: Point, visible: Boolean) {
        val treetopGrid = readFile("day8/exampleInput.txt").toTreetopGrid()
        assertThat(isVisibleFromOutsideChecker.invoke(point,treetopGrid)).isEqualTo(visible)
    }

    @ParameterizedTest(name = "Tree at:  \"{0}\" has scene score of: \"{1}\"")
    @MethodSource("testScenicScore")
    fun `test scenic score`(point: Point, score: Int) {
        val treetopGrid = readFile("day8/exampleInput.txt").toTreetopGrid()
        assertThat(scenicScoreChecker.invoke(point,treetopGrid)).isEqualTo(score)
    }

    companion object {
        @JvmStatic
        fun testTrees() = listOf(
                Arguments.of(Point(3, 4), mapOf(
                        RIGHT to listOf(0),
                        LEFT to listOf(3, 5, 3),
                        TOP to listOf(4, 3, 1, 7),
                        BOTTOM to emptyList(),
                )),
                Arguments.of(Point(2, 2), mapOf(
                        RIGHT to listOf(3, 2),
                        LEFT to listOf(5, 6),
                        TOP to listOf(5, 3),
                        BOTTOM to listOf(5, 3),
                )),
                Arguments.of(Point(2, 1), mapOf(
                        RIGHT to listOf(1, 2),
                        LEFT to listOf(5, 2),
                        TOP to listOf(3),
                        BOTTOM to listOf(3, 5, 3),
                )),
                Arguments.of(Point(0, 0), mapOf(
                        RIGHT to listOf(0, 3, 7, 3),
                        LEFT to listOf(),
                        TOP to listOf(),
                        BOTTOM to listOf(2, 6, 3, 3),
                )),
        )

        @JvmStatic
        fun testDistanceToEdge() = listOf(
                Arguments.of(Point(2, 1), TOP, 1),
                Arguments.of(Point(2, 1), RIGHT, 2),
                Arguments.of(Point(2, 1), BOTTOM, 3),
                Arguments.of(Point(2, 1), LEFT, 2),
        )

        @JvmStatic
        fun testTreesLineOfSights() = listOf(
                Arguments.of(Point(0, 0), true),
                Arguments.of(Point(1, 1), true),
                Arguments.of(Point(2, 1), true),
                Arguments.of(Point(1, 2), true),
                Arguments.of(Point(3, 1), false),
                Arguments.of(Point(2, 2), false),
                Arguments.of(Point(3, 2), true),
                Arguments.of(Point(2, 3), true),
                Arguments.of(Point(4, 4), true),
        )

        @JvmStatic
        fun testScenicScore() = listOf(
                Arguments.of(Point(2, 1), 4),
                Arguments.of(Point(2, 3), 8),
        )
    }
}

