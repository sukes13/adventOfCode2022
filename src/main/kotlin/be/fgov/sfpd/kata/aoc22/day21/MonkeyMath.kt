package be.fgov.sfpd.kata.aoc22.day21

import be.fgov.sfpd.kata.aoc22.day21.MathMonkey.OperationMonkey
import be.fgov.sfpd.kata.aoc22.day21.MathMonkey.ValueMonkey
import be.fgov.sfpd.kata.aoc22.day21.MonkeyOperator.*
import be.fgov.sfpd.kata.aoc22.mapLines


fun part1(input: String) = input.toMathMonkeys().findYellOf("root").first

fun part2(input: String) = input.toMathMonkeys().prepareYellingFor("root", "humn").findYellOf("humn").first

fun List<MathMonkey>.findYellOf(targetMonkey: String): Pair<Long, List<MathMonkey>> {
    val valueMonkeys = filterIsInstance<ValueMonkey>().associate { it.name to it.value }.toMutableMap()
    val operationMonkeys = filterIsInstance<OperationMonkey>().toMutableList()

    while (targetMonkey !in valueMonkeys.keys) {
        operationMonkeys.filter { it.valuesFound(valueMonkeys.keys) }.let { monkeysThatCanYell ->
            if (monkeysThatCanYell.isEmpty()) {
                return -1L to valueMonkeys.map { ValueMonkey(it.key, it.value) }
            } else {
                monkeysThatCanYell.forEach {
                    valueMonkeys[it.name] = it.operator.executeFor(valueMonkeys[it.nameMonkey1]!!, valueMonkeys[it.nameMonkey2]!!)
                    operationMonkeys.remove(it)
                }
            }
        }
    }

    return valueMonkeys.filterKeys { it == targetMonkey }.values.single() to operationMonkeys
}

private fun List<MathMonkey>.prepareYellingFor(root: String, me: String): List<MathMonkey> {
    val rootMonkey = single { it.name == root } as OperationMonkey
    val attemptForMonkey1 = filter { it.name != me }.findYellOf(rootMonkey.nameMonkey1)
    val attemptForMonkey2 = filter { it.name != me }.findYellOf(rootMonkey.nameMonkey2)
    val (rootYell, unsolvedOperations) = if (attemptForMonkey1.first == -1L) attemptForMonkey2 else attemptForMonkey1
    val (_, valueMonkeysAfterAttempt) = if (attemptForMonkey1.first == -1L) attemptForMonkey1 else attemptForMonkey2

    return unsolvedOperations.filterIsInstance<OperationMonkey>().reverseOperations()
            .plus(valueMonkeysAfterAttempt)
            .plus(ValueMonkey(rootMonkey.nameMonkey1, rootYell))
            .plus(ValueMonkey(rootMonkey.nameMonkey2, rootYell))
}

private fun List<OperationMonkey>.reverseOperations() = flatMap { operationMonkey ->
    listOf(operationMonkey.reverseForMonkey1(), operationMonkey.reverseForMonkey2())
}

sealed class MathMonkey {
    abstract val name: String

    data class ValueMonkey(override val name: String, val value: Long) : MathMonkey()

    data class OperationMonkey(override val name: String, val nameMonkey1: String, val nameMonkey2: String, val operator: MonkeyOperator) : MathMonkey() {
        fun valuesFound(valueMonkeys: MutableSet<String>) = nameMonkey1 in valueMonkeys && nameMonkey2 in valueMonkeys

        fun reverseForMonkey1() = OperationMonkey(name = nameMonkey1, nameMonkey1 = name, nameMonkey2 = nameMonkey2,
                operator = when (operator) {
                    PLUS -> MINUS
                    MINUS -> PLUS
                    MULTIPLY -> DIVIDE
                    DIVIDE -> MULTIPLY
                }
        )

        fun reverseForMonkey2(): OperationMonkey {
            val base = OperationMonkey(name = nameMonkey2, nameMonkey1 = name, nameMonkey2 = nameMonkey1, operator = PLUS)
            return when (operator) {
                PLUS -> base.copy(operator = MINUS)
                MINUS -> base.copy(nameMonkey1 = base.nameMonkey2, nameMonkey2 = base.nameMonkey1, operator = MINUS)
                MULTIPLY -> base.copy(operator = DIVIDE)
                DIVIDE -> base.copy(nameMonkey1 = base.nameMonkey2, nameMonkey2 = base.nameMonkey1, operator = DIVIDE)
            }
        }
    }
}

enum class MonkeyOperator(val sign: String, val executeFor: (Long, Long) -> Long) {
    PLUS("+", { a, b -> a + b }),
    MINUS("-", { a, b -> a - b }),
    MULTIPLY("*", { a, b -> a * b }),
    DIVIDE("/", { a, b -> a / b });

    companion object {
        fun bySign(sign: String) = values().find { it.sign == sign } ?: error("Invalid sign found: $sign")
    }
}

internal fun String.toMathMonkeys(): List<MathMonkey> = mapLines { line ->
    val (name, leftOver) = line.split(": ").take(2)
    when {
        line.length < 14 -> ValueMonkey(name, leftOver.toLong())
        else -> {
            val (nameMonkey1, sign, nameMonkey2) = leftOver.split(" ").take(3)
            OperationMonkey(name, nameMonkey1, nameMonkey2, MonkeyOperator.bySign(sign))
        }
    }
}
