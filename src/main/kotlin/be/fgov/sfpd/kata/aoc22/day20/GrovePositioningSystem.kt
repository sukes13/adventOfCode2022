package be.fgov.sfpd.kata.aoc22.day20


fun part1(input: String): Int {
    val file = input.toGPSFile().decode()
    val indexOf0 = file.indexOfFirst { it.value == 0 }

    return listOf(1000, 2000, 3000).sumOf { file[ indexOf0.rolloverIndexFor(it, file.size) ].value }
}

fun part2(input: String) = 0


fun List<GPSNumber>.decode(): List<GPSNumber> {
    val rotatingList = toMutableList()

    forEach { number ->
        rotatingList.indexOf(number).let { index ->
            rotatingList.removeAt(index)
            rotatingList.add(index.rolloverIndexFor(number.value, size - 1), number)
        }
    }
    return rotatingList
}

data class GPSNumber(val id: Int, val value: Int) {
    override fun toString() = "$value"
}

fun String.toGPSFile() = lines().mapIndexed { index, line ->
    GPSNumber(index, line.toInt())
}

private fun Int.rolloverIndexFor(next: Int, size: Int) = (this + next).mod(size)
