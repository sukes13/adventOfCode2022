package be.fgov.sfpd.kata.aoc22.day15

import be.fgov.sfpd.kata.aoc22.Point
import be.fgov.sfpd.kata.aoc22.flatMapLines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue


//fun part1(input: String, line: Int) = input.toBeaconSensors().pointsInRange(line)
fun part2(input: String, maxForPart: Int): Long {
    val distressSignal = input.toBeaconSensors().distressSignalIn(maxForPart)
    println("RESULT = $distressSignal")
    return distressSignal.x.toLong() * 4_000_000L + distressSignal.y
}

fun List<BeaconSensor>.distressSignalIn(max: Int): Point =
        (0..max).mapNotNull { y ->
            if (y % 200_000 == 0) println("Reached line: $y")
            notInRangeOn(y)?.let {
                println("Signal found : x=${it + 1}, y=$y")
                Point(it + 1, y)
            }
        }.singleOrNull() ?: error("Multiple points found")

private fun List<BeaconSensor>.notInRangeOn(line: Int): Int? {
    val allRangesOn = allRangesOn(line)
    return allRangesOn.fold(mutableListOf(allRangesOn.first())) { acc, range ->
        acc += acc.last().filterOverlap(range)
        acc
    }.findGapOrNull()
}

private fun MutableList<IntRange>.findGapOrNull() =
        distinct().windowed(2, 1).firstNotNullOfOrNull { (intRange, b) ->
            if (intRange.last + 1 < b.first) intRange.last else null
        }

private fun IntRange.filterOverlap(other: IntRange): IntRange =
        when {
            this fullyOverlapses other -> other
            other fullyOverlapses this -> this
            this overlapses other -> last..other.last
            first <= other.first -> other
            else -> this
        }

private infix fun IntRange.overlapses(other: IntRange) = first <= other.last && other.first <= last && last <= other.last

private infix fun IntRange.fullyOverlapses(other: IntRange) = first >= other.first && last <= other.last

private fun List<BeaconSensor>.allRangesOn(line: Int): Sequence<IntRange> =
        asSequence().mapNotNull { sensor ->
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

fun IntRange.mapParallel(f: suspend (Int) -> Point?): List<Point> = runBlocking {
    map { async(Dispatchers.Default) { f(it) } }.mapNotNull { it.await() }
}