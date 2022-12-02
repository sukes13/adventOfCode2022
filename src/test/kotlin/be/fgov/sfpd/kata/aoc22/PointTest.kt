package be.fgov.sfpd.kata.aoc22

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PointTest {

    @Test
    fun `given two points - we can add them together`() {
        val origin = Point(0, 0)
        val vector = Point(-1, 1)

        val actual = origin + vector

        assertThat(actual).isEqualTo(Point(-1, 1))
    }

    @Test
    fun `return neighbouring points of point`() {
        val point = Point(0, 0)

        val actual = point.neighbours

        assertThat(actual).containsExactlyInAnyOrder(
            Point(-1, -1), Point(0, -1), Point(1, -1),
            Point(-1, 0), Point(1, 0),
            Point(-1, 1), Point(0, 1), Point(1, 1)
        )
    }


    @Test
    fun `return orthogonal neighbouring points of point`() {
        val point = Point(0, 0)

        val actual = point.orthogonalNeighbours

        assertThat(actual).containsExactlyInAnyOrder(
            Point(0, -1),
            Point(-1, 0), Point(1, 0),
            Point(0, 1)
        )
    }
}