package be.fgov.sfpd.kata.aoc22.day5

import be.fgov.sfpd.kata.aoc22.flatMapLines
import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.spitOnEmptyLine

typealias Cargo = Map<Int, List<String>>

fun part1(input: String): String {
    val startCargo = input.spitOnEmptyLine().first().toCargo()
    val moves = input.spitOnEmptyLine().last().toCrateMoves()

    return startCargo.executeMoves(moves){ it }
}

fun part2(input: String) : String {
    val startCargo = input.spitOnEmptyLine().first().toCargo()
    val moves = input.spitOnEmptyLine().last().toCrateMoves()

    return startCargo.executeMoves(moves) { it.reversed() }
}

private fun Map<Int, List<String>>.executeMoves(moves: List<CrateMove>,crane: (List<String>) -> List<String>): String {
    return moves.fold(this) { cargo, move ->
        cargo.execute(move, crane)
    }.getTopCrates()
}

fun Cargo.execute(crateMove: CrateMove,crane: (List<String>) -> List<String>): Cargo {
    val movingCrates = this[crateMove.from - 1]?.take(crateMove.number) ?: listOf()
    return mapValues { (stackNr, stack) ->
        when (stackNr) {
            crateMove.from - 1 -> stack.drop(crateMove.number)
            crateMove.to - 1 -> listOf(crane(movingCrates).reversed(), stack).flatten()
            else -> stack
        }
    }
}

fun Cargo.getTopCrates() = toSortedMap().map { it.value.firstOrNull() ?: "" }.joinToString("")

data class CrateMove(val number: Int, val from: Int, val to: Int)

fun String.toCargo() = parseForCargo()
        .fold(mutableMapOf<Int, List<String>>()) { cargo, line ->
            line.forEachIndexed { stack, crate ->
                if (crate.isNotBlank()) cargo[stack] = cargo[stack]?.plus(crate) ?: listOf(crate)
            }
            cargo
        }.toMap()

private fun String.parseForCargo() = mapLines { line ->
    line.chunked(4).map { it.replace("\\s|\\[|\\]".toRegex(), "") }
}.dropLast(1)

fun String.toCrateMoves() = flatMapLines { moveStr ->
    moveStr.replace("move |from |to ".toRegex(), ",")
            .drop(1)
            .split(",")
            .map { it.trim().toInt() }
            .chunked(3)
            .map { (number, from, to) -> CrateMove(number, from, to) }
}

