package be.fgov.sfpd.kata.aoc22.day15

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.flatMapLines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue


fun part1(input: String, max: Int) = input.toBeaconSensors().pointsInRange(max)
fun part2(input: String, maxForPart: Int): Long {
    val distressSignal = input.toBeaconSensors().distressSignalIn(maxForPart)
    println("RESUL? = ${distressSignal.x} , ${distressSignal.y}")
    return distressSignal.x.toLong() * 4_000_000L + distressSignal.y
}

fun List<BeaconSensor>.distressSignalIn(max: Int): Point {
    return (0..max).mapParallel { y ->
        (0..max).filter { x ->
            !Point(x, y).inRange(this)
        }.map { Point(it, y) }.singleOrNull().also { if (y % 500 == 0) println("$y rows checked found: $it") }
    }.single()
}

fun IntRange.mapParallel(f: suspend (Int) -> Point?): List<Point> = runBlocking {
    map { async(Dispatchers.Default) { f(it) } }.mapNotNull { it.await() }
}

fun List<BeaconSensor>.pointsInRange(line: Int): Int {
    val maxRange = maxBy { it.range }.range
    val minX = minBy { it.point.x }.point.x - maxRange
    val maxX = maxBy { it.point.x }.point.x + maxRange
    val occupied = filter { it.point.y == line }.size + map { it.beacon }.filter { it.y == line }.distinct().size

    return (minX..maxX).filter { Point(it, line).inRange(this) }.toList().size - occupied
}

private fun Point.inRange(sensors: List<BeaconSensor>): Boolean {
    return sensors.asSequence().map { it.point.taxiDistanceTo(this) <= it.range }.reduce { acc, next -> acc || next }
}

fun Point.taxiDistanceTo(other: Point) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

fun String.toBeaconSensors(): List<BeaconSensor> = flatMapLines { it.toBeaconSensor() }
private fun String.toBeaconSensor() =
        substringAfter("Sensor at x=").trim().split(": closest beacon is at x=").flatMap {
            it.split(", y=").windowed(2).map { (x, y) -> Point(x.toInt(), y.toInt()) }
        }.windowed(2).map { (sensor, beacon) -> BeaconSensor(sensor, beacon, sensor.taxiDistanceTo(beacon)) }

data class BeaconSensor(val point: Point, val beacon: Point, val range: Int)