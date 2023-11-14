package be.fgov.sfpd.kata.aoc22

fun readFile(fileName: String): String =
    {}::class.java.classLoader.getResourceAsStream(fileName)?.reader()?.readText() ?: error("Could not load $fileName")

fun <T> String.mapLines(variant: (String) -> T) = lines().map(variant)

fun <T> String.flatMapLines(variant: (String) -> Iterable<T>) = lines().flatMap(variant)

fun String.filterLines(variant: (String) -> Boolean) = lines().filter(variant)

fun String.splitOnEmptyLine() = split("\r\n\r\n")

/**
 *@receiver Returns set of all elements shared in all sets
 */
fun <T> Iterable<Set<T>>.overlap(): Set<T> =
        fold(first().toSet()) { shared, element ->
            shared intersect element.toSet()
        }


