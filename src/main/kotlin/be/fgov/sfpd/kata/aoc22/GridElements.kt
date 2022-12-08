package be.fgov.sfpd.kata.aoc22


typealias Grid<T> = Map<Int, List<T>>

fun <T, V> Grid<T>.checkEveryPointFor(checker: (Point, Grid<T>) -> V?) =
        (0 until this.size).flatMap { x ->
            (0 until this.size).mapNotNull { y ->
                checker(Point(x, y), this)
            }
        }

fun <T> Grid<T>.get(point: Point) : T = this[point.y]?.get(point.x) ?: error("Point: $point does not exist in grid")
fun <T> Grid<T>.column(number: Int) = values.map { it.elementAtOrNull(number) ?: error("Column: $it does not exist in grid") }
fun <T> Grid<T>.row(number: Int) = this[number] ?: error("Row: $number does not exist in grid")

data class Point(val x: Int, val y: Int) {
    //@formatter:off
    val neighbours: Set<Point>
        get() = listOf(
                Point(-1, -1), Point(0, -1), Point(1, -1),
                Point(-1, 0), Point(1, 0),
                Point(-1, 1), Point(0, 1), Point(1, 1),
        ).map { vector -> this + vector }
                .toSet()

    val orthogonalNeighbours: Set<Point>
        get() = listOf(
                Point(0, -1),
                Point(-1, 0), Point(1, 0),
                Point(0, 1),
        ).map { vector -> this + vector }
                .toSet()
    //@formatter:on

    operator fun plus(vector: Point) = Point(this.x + vector.x, this.y + vector.y)
}
