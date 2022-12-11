package be.fgov.sfpd.kata.aoc22.day11

import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String) = input.toMonkeys()





fun String.toMonkeys(): Map<Int, Monkey> = this.splitOnEmptyLine().map { monkeyString ->
    val id = monkeyString.lines()[0].findMonkeyId()
    val items = monkeyString.lines()[1].monkeyItems()
    val operation: (Int) -> Int = monkeyString.lines()[2].monkeyOperation()
    val monkeyCheckValue: Int = monkeyString.lines()[3].drop(21).trim().toInt()
    val throwToMonkey1: Int = monkeyString.lines()[4].drop(29).trim().toInt()
    val throwToMonkey2: Int = monkeyString.lines()[5].drop(30).trim().toInt()

    Monkey(id, items, operation, MonkeyCheck(monkeyCheckValue, throwToMonkey1, throwToMonkey2))
}.associateBy { it.id }

private fun String.monkeyItems() = drop(18).split(", ").map { it.toInt() }

private fun String.findMonkeyId() = drop(6).dropLast(1).trim().toInt()

private fun String.monkeyOperation(): (Int) -> Int = drop(23).split(" ").let { (operator, value) -> { it.doOperation(operator, value.toInt()) } }

private fun Int.doOperation(operator: String, other: Int) =
        when (operator) {
            "+" -> this + other
            "*" -> this * other
            else -> error("Inknown operator found: $operator")
        }

data class Monkey(val id: Int, val items: List<Int>, val operation: (Int) -> Int, val check: MonkeyCheck, val inspections: Int = 0) {

}

data class MonkeyCheck(val check: Int, val monkey1: Int, val monkey2: Int) {

}

fun part2(input: String) = ""

