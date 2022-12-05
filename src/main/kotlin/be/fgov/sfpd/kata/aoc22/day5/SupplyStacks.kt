package be.fgov.sfpd.kata.aoc22.day5

import be.fgov.sfpd.kata.aoc22.flatMapLines
import be.fgov.sfpd.kata.aoc22.spitOnEmptyLine

typealias Cargo = Map<Int, List<String>>

data class CrateMove(val number: Int, val from: Int, val to: Int)

fun part1(input: String): String {
    val startCargo = input.spitOnEmptyLine().first().toCargo()
    val moves = input.spitOnEmptyLine().last().toCrateMoves()

    return startCargo.executeMoves(moves, craneOrder = { it.reversed() }).getTopCrates()
}

fun part2(input: String): String {
    val startCargo = input.spitOnEmptyLine().first().toCargo()
    val moves = input.spitOnEmptyLine().last().toCrateMoves()

    return startCargo.executeMoves(moves, craneOrder = { it }).getTopCrates()
}

private fun Cargo.executeMoves(moves: List<CrateMove>, craneOrder: (List<String>) -> List<String>) =
        moves.fold(this) { cargo, move ->
            cargo.execute(move, craneOrder)
        }

fun Cargo.execute(crateMove: CrateMove, craneOrder: (List<String>) -> List<String>): Cargo {
    val movingCrates = craneOrder( get(crateMove.from - 1)?.take(crateMove.number) ?: listOf() )
    return mapValues { (stackNr, stack) ->
        when (stackNr) {
            crateMove.from - 1 -> stack.drop(crateMove.number)
            crateMove.to - 1 -> listOf(movingCrates, stack).flatten()
            else -> stack
        }
    }
}

fun Cargo.getTopCrates() = toSortedMap().map { it.value.firstOrNull() ?: "" }.joinToString("")

fun String.toCargo() =
        lines().dropLast(1)
                .fold(mutableMapOf<Int, List<String>>()) { cargo, line ->
                    line.chunked(4)
                        .map { it.replace("\\s|\\[|]".toRegex(), "") }
                        .forEachIndexed { stack, crate ->
                            if (crate.isNotBlank())
                                cargo[stack] = cargo[stack]?.plus(crate) ?: listOf(crate)
                        }
                    cargo
                }.toMap()

fun String.toCrateMoves() = flatMapLines { moveStr ->
    moveStr.replace("move | from | to ".toRegex(), ",")
            .drop(1)
            .split(",")
            .map(String::toInt)
            .chunked(3)
            .map { (number, from, to) -> CrateMove(number, from, to) }
}

