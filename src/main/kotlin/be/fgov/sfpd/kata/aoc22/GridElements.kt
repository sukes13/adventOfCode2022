package be.fgov.sfpd.kata.aoc22


typealias Grid<T> = Map<Int, List<T>>

fun <T, V> Grid<T>.checkEveryPointFor(checker: (Point, Grid<T>) -> V?) =
        (0 until this.size).flatMap { y ->
            (0 until this.size).mapNotNull { x ->
                checker(Point(x, y), this)
            }
        }

fun <T> Grid<T>.get(point: Point): T = this[point.y]?.get(point.x) ?: error("Point: $point does not exist in grid")
fun <T> Grid<T>.column(point: Point) = values.map {
    it.elementAtOrNull(point.x) ?: error("Column: $it does not exist in grid")
}

fun <T> Grid<T>.row(point: Point) = this[point.y] ?: error("Row: $point.x does not exist in grid")

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

    fun stepInDirection(direction: Direction) =
            when (direction) {
                Direction.UP -> copy(y = y + 1)
                Direction.RIGHT -> copy(x = x + 1)
                Direction.DOWN -> copy(y = y - 1)
                Direction.LEFT -> copy(x = x - 1)
            }

    fun touching(point: Point) = point in this.neighbours || point == this

}

infix fun Point.onSameRowAs(other: Point) = y == other.y
infix fun Point.onSameColumnAs(other: Point) = x == other.x
infix fun Point.moreToLeft(other: Point) = x > other.x
infix fun Point.lowerThan(other: Point) = y > other.y

fun List<Point>.visualize(gridWidth: Int, gridHeight: Int): String =
        (gridHeight downTo -gridHeight).map { y ->
            (-gridWidth until gridWidth).mapNotNull { x ->
                when {
                    Point(x, y) in this -> "#"
                    Point(x, y) == Point(0, 0) -> "s"
                    else -> "."
                }
            }
        }.joinToString("\n") { it.joinToString("") }


enum class Direction {
    UP, RIGHT, DOWN, LEFT;
}