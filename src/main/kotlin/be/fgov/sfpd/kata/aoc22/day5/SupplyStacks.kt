package be.fgov.sfpd.kata.aoc22.day5

import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.spitOnEmptyLine

fun part1(input: String): Nothing = TODO()

fun part2(input: String): Nothing = TODO()

typealias Cargo = Map<Int, List<String>>

data class Ship(val cargo: Cargo, val moves: List<CrateMove>) {

}
data class CrateMove(val number: Int, val from: Int, val to: Int) {

}

fun String.toShip(): Ship {
    val (cargoStr, movesStr) = spitOnEmptyLine()
    return Ship(cargoStr.toCargo(), movesStr.toCrateMoves())
}

fun String.toCargo() = parseForCargo()
        .fold(mutableMapOf<Int, List<String>>()) { cargo, line ->
            line.forEachIndexed { stack, crate ->
                if (crate.isNotBlank()) cargo[stack] = cargo[stack]?.plus(crate) ?: listOf(crate)
            }
            cargo
        }.toSortedMap()

private fun String.parseForCargo() = mapLines { it.chunked(4).map { it.replace("\\s|\\[|\\]".toRegex(), "") } }.dropLast(1)

fun String.toCrateMoves() = lines().flatMap { moveStr ->
    moveStr.parseForMoves()
            .let { move ->
                move.split(",")
                        .map { it.trim().toInt() }
                        .chunked(3)
                        .map { (number, from, to) ->
                            CrateMove(number, from, to)
                        }
            }
}

private fun String.parseForMoves() = replace("move |from |to ".toRegex(), ",").drop(1)

