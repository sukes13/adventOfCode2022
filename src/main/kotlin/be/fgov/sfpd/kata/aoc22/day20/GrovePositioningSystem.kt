package be.fgov.sfpd.kata.aoc22.day20


fun part1(input: String) = input.toGPSFile().decodeRepeated(1).getGroveCoordinates()

fun part2(input: String) = input.toGPSFile().map { it.copy(value = it.value * 811589153L) }.decodeRepeated(10).getGroveCoordinates()

private fun List<GPSNumber>.getGroveCoordinates(): Long {
    val indexOf0 = indexOfFirst { it.value == 0L }
    return listOf(1000L, 2000L, 3000L).sumOf { this[indexOf0.rolloverIndexFor(it, size)].value }
}

fun List<GPSNumber>.decodeRepeated(repeats: Int = 1): List<GPSNumber> {
    println("Start file = $this")
    return (1..repeats).fold(this) { file, repeat ->
        file.decode(this).also { println("After $repeat = $it") }
    }
}

private fun List<GPSNumber>.decode(startFile: List<GPSNumber>): List<GPSNumber> {
    val rotatingList = toMutableList()

    startFile.forEach { number ->
        rotatingList.indexOf(number).let { index ->
            rotatingList.removeAt(index)
            rotatingList.add(index.rolloverIndexFor(number.value, size - 1), number)
        }
    }

    return rotatingList
}

private fun Int.rolloverIndexFor(next: Long, size: Int) = (this + next).mod(size)

data class GPSNumber(val id: Int, val value: Long) {
    override fun toString() = "$value"
}

fun String.toGPSFile() = lines().mapIndexed { index, line ->
    GPSNumber(id = index, value = line.toLong())
}

