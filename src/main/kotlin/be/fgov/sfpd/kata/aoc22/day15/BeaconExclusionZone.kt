package be.fgov.sfpd.kata.aoc22.day15

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.flatMapLines
import kotlin.math.absoluteValue


fun part1(input: String, line: Int) = input.toBeaconSensors().pointsInRangeOn(line)

fun part2(input: String, maxForPart: Int): Long {
    val distressSignal = input.toBeaconSensors().pointOutOfRangeIn(maxForPart)
    println("RESULT = $distressSignal")
    return distressSignal.x.toLong() * 4_000_000L + distressSignal.y
}

private fun List<BeaconSensor>.pointsInRangeOn(line: Int): Int {
    val beaconsOnLine = filter { it.beacon.y == line }.map { it.beacon }.distinct().size
    val rangesOnLine = allRangesOn(line).sortWithoutOverlap()
    return (rangesOnLine.first().first..rangesOnLine.last().last).count() - beaconsOnLine
}

private fun List<BeaconSensor>.pointOutOfRangeIn(max: Int) =
        (0..max).mapNotNull { line ->
            notInRangeOn(line).also { if (line % 200_000 == 0) println("Checked until line: ${"%,d".format(line)}") }
        }.singleOrNull() ?: error("None or multiple points found")

private fun List<BeaconSensor>.notInRangeOn(line: Int): Point? =
        allRangesOn(line)
                .sortWithoutOverlap()
                .findGapOrNull()?.let {
                    println("Signal found at $it")
                    Point(it, line)
                }

private fun List<IntRange>.sortWithoutOverlap(): List<IntRange> =
        fold(mutableListOf(this.first())) { acc, range ->
            val previous = acc.last()
            acc += when {
                previous fullyOverlaps range -> range
                range fullyOverlaps previous -> previous
                previous overlaps range -> previous.last..range.last
                previous.first <= range.first -> range
                else -> previous
            }
            acc
        }.distinct()

private infix fun IntRange.fullyOverlaps(other: IntRange) = first >= other.first && last <= other.last

private infix fun IntRange.overlaps(other: IntRange) = first <= other.last && other.first <= last && last <= other.last

private fun List<IntRange>.findGapOrNull() = windowed(2, 1).firstNotNullOfOrNull { (a, b) ->
    if (a.last + 1 < b.first) a.last + 1 else null
}

private fun List<BeaconSensor>.allRangesOn(line: Int): List<IntRange> =
        mapNotNull { sensor ->
            val yDist = (sensor.point.y - line).absoluteValue
            if (sensor.range >= yDist) {
                (sensor.point.x - (sensor.range - yDist)..sensor.point.x + (sensor.range - yDist))
            } else null
        }.sortedBy { it.first }

data class BeaconSensor(val point: Point, val beacon: Point, val range: Int)

fun String.toBeaconSensors(): List<BeaconSensor> = flatMapLines { line ->
    line.substringAfter("Sensor at x=").trim().split(": closest beacon is at x=")
            .flatMap {
                it.split(", y=").windowed(2).map { (x, y) -> Point(x.toInt(), y.toInt()) }
            }.windowed(2).map { (sensor, beacon) ->
                BeaconSensor(point = sensor, beacon = beacon, range = (sensor.x - beacon.x).absoluteValue + (sensor.y - beacon.y).absoluteValue)
            }
}

//fun IntRange.mapParallel(f: suspend (Int) -> Point?): List<Point> = runBlocking {
//    map { async(Dispatchers.Default) { f(it) } }.mapNotNull { it.await() }
//}