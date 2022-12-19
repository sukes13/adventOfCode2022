package be.fgov.sfpd.kata.aoc22.day15

import be.fgov.sfpd.kata.aoc22.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class BeaconExclusionZoneTest {

    @Test
    fun `test taxiDistanceTo`() {
        assertThat(Point(0, 0).taxiDistanceTo(Point(6, 5))).isEqualTo(11)
    }

    @Test
    fun `test toBeaconSensors`() {
        assertThat("""Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16""".toBeaconSensors()).containsExactly(
                BeaconSensor(Point(2, 18) ,Point(-2, 15), Point(2, 18).taxiDistanceTo(Point(-2, 15))),
                BeaconSensor(Point(9, 16) ,Point(10, 16), Point(10, 16).taxiDistanceTo(Point(9, 16))),
        )
    }

//    @Test
//    fun `test part1 points`() {
//        val input = readFile("day15/exampleInput.txt")
//        assertThat(input.toBeaconSensors().pointsInRange(10)).containsExactly(
//                *(-2..24).map { Point(it,10) }.toTypedArray()
//        )
//    }

}
