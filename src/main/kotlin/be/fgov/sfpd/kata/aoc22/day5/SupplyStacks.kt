package be.fgov.sfpd.kata.aoc22.day5

import be.fgov.sfpd.kata.aoc22.flatMapLines
import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.spitOnEmptyLine

fun part1(input: String): Nothing = TODO()

fun part2(input: String): Nothing = TODO()

typealias Cargo = Map<Int, List<String>>

data class Ship(val cargo: Cargo) {
    fun execute(crateMove: CrateMove): Ship {
        val movingCrates = cargo[crateMove.from - 1]?.take(crateMove.number) ?: listOf()
        val newCargo = cargo.mapValues { (stackNr,stack) ->
            when(stackNr){
                crateMove.from - 1 -> stack.drop(crateMove.number)
                crateMove.to- 1 -> listOf(movingCrates.reversed() , stack).flatten()
                else -> stack
            }
        }
        return Ship(cargo = newCargo)
    }
}

data class CrateMove(val number: Int, val from: Int, val to: Int)

fun String.toShip(): Ship {
    val (cargoStr, _) = spitOnEmptyLine()
    return Ship(cargoStr.toCargo())
}

fun String.toCargo() = parseForCargo()
        .fold(mutableMapOf<Int, List<String>>()) { cargo, line ->
            line.forEachIndexed { stack, crate ->
                if (crate.isNotBlank()) cargo[stack] = cargo[stack]?.plus(crate) ?: listOf(crate)
            }
            cargo
        }.toSortedMap()

private fun String.parseForCargo() = mapLines { line ->
    line.chunked(4).map { it.replace("\\s|\\[|\\]".toRegex(), "") }
}.dropLast(1)

fun String.toCrateMoves() = flatMapLines { moveStr ->
    moveStr.replace("move |from |to ".toRegex(), ",").drop(1)
            .let { move ->
                move.split(",")
                        .map { it.trim().toInt() }
                        .chunked(3)
                        .map { (number, from, to) -> CrateMove(number, from, to) }
            }
}

