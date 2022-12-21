package be.fgov.sfpd.kata.aoc22.day15

import be.fgov.sfpd.kata.aoc22.Point
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class BeaconExclusionZoneTest {
    @Test
    fun `test toBeaconSensors`() {
        assertThat("""Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16""".toBeaconSensors()).containsExactly(
                BeaconSensor(Point(2, 18) ,Point(-2, 15), 7),
                BeaconSensor(Point(9, 16) ,Point(10, 16), 1),
        )
    }
}
