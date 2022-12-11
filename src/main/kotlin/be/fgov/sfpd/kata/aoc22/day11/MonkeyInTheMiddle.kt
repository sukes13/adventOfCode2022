package be.fgov.sfpd.kata.aoc22.day11

import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

typealias Monkeys = Map<Int, Monkey>

fun part1(input: String) = input.toMonkeys().doNumberOfSteps(20, 3).monkeyBusiness()

fun part2(input: String) = input.toMonkeys().doNumberOfSteps(10000, 1).monkeyBusiness()

private fun Monkeys.monkeyBusiness() = values.map { it.inspections }.sorted().takeLast(2).windowed(2).map { (a, b) -> a * b }.single()

private fun Monkeys.doNumberOfSteps(number: Int, relief: Int): Monkeys {
    val commonModulus = this.values.fold(1L) { modulus, m -> modulus * m.check.value }
    var monkeys = this

    (0 until number).forEach { _ ->
        monkeys = monkeys.doRound(commonModulus, relief)
    }

    return monkeys
}


fun Monkeys.doRound(commonModulus: Long, relief: Int): Monkeys {
    val currentMonkeys = this.toMutableMap()

    for (monkeyId in currentMonkeys.keys) {
//        println("Monkey ${monkeyId}: ${currentMonkeys[monkeyId]!!.inspections}")
        for (item in currentMonkeys[monkeyId]!!.items) {
//            println("  - inspects item: $item.")
            val monkey = currentMonkeys[monkeyId]!!
            val updatedWorry = monkey.operation(item) / relief % commonModulus
//                    .also { println("    but worry drops to: $it") }
            val throwTo = if (updatedWorry % monkey.check.value == 0L) monkey.check.monkey1 else monkey.check.monkey2
//                    .also { println("    Check divisible by ${monkey.check.value} and throws to Monkey: $it ") }

            currentMonkeys[monkeyId] = monkey.copy(items = monkey.items.drop(1), inspections = monkey.inspections + 1)

            currentMonkeys[throwTo] = currentMonkeys[throwTo]?.copy(items = currentMonkeys[throwTo]!!.items.plus(updatedWorry))
                    ?: error("Cannot throw to Monkey: ${monkeyId}")
        }
    }
    return currentMonkeys.toMap()
}

fun String.toMonkeys(): Monkeys = this.splitOnEmptyLine().map { monkeyString ->
    val id = monkeyString.lines()[0].findMonkeyId()
    val items = monkeyString.lines()[1].monkeyItems()
    val operation: (Long) -> Long = monkeyString.lines()[2].monkeyOperation()
    val monkeyCheckValue: Int = monkeyString.lines()[3].drop(21).trim().toInt()
    val throwToMonkey1: Int = monkeyString.lines()[4].drop(29).trim().toInt()
    val throwToMonkey2: Int = monkeyString.lines()[5].drop(30).trim().toInt()

    Monkey(id, items, operation, MonkeyCheck(monkeyCheckValue, throwToMonkey1, throwToMonkey2))
}.associateBy { it.id }

private fun String.findMonkeyId() = drop(6).dropLast(1).trim().toInt()
private fun String.monkeyItems() = drop(18).split(", ").map { it.toLong() }
private fun String.monkeyOperation(): (Long) -> Long = drop(23).split(" ")
        .let { (operator, value) ->
            {
                val other = if (value == "old") it else value.toLong()
                when (operator) {
                    "+" -> it + other
                    "*" -> it * other
                    else -> error("Unknown operator found: $operator")
                }
            }
        }

data class Monkey(val id: Int, val items: List<Long>, val operation: (Long) -> Long, val check: MonkeyCheck, val inspections: Long = 0L)

data class MonkeyCheck(val value: Int, val monkey1: Int, val monkey2: Int)