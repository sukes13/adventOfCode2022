package be.fgov.sfpd.kata.aoc22.day21

import be.fgov.sfpd.kata.aoc22.day21.MathMonkey.OperationMonkey
import be.fgov.sfpd.kata.aoc22.day21.MathMonkey.ValueMonkey
import be.fgov.sfpd.kata.aoc22.mapLines

fun part1(input: String) = input.toMathMonkeys().findYellOf("root")

fun part2(input: String) = 0

fun List<MathMonkey>.findYellOf(targetMonkey: String): Long {
    val valueMonkeys = filterIsInstance<ValueMonkey>().associate { it.name to it.value }.toMutableMap()
    val operationMonkeys = filterIsInstance<OperationMonkey>().toMutableList()

    while(targetMonkey !in valueMonkeys.keys){
        operationMonkeys.filter { it.valuesFound(valueMonkeys.keys)}.forEach {
            valueMonkeys[it.name] = it.operator.executeFor(valueMonkeys[it.nameMonkey1]!!,valueMonkeys[it.nameMonkey2]!!)
            operationMonkeys.remove(it)
        }
    }

    return valueMonkeys.filterKeys { it == targetMonkey }.values.single()
}


sealed class MathMonkey {
    abstract val name: String

    data class ValueMonkey(override val name: String, val value: Long) : MathMonkey()
    data class OperationMonkey(override val name: String, val nameMonkey1: String, val nameMonkey2: String, val operator: MonkeyOperator) : MathMonkey(){
        fun valuesFound(valueMonkeys: MutableSet<String>) = nameMonkey1 in valueMonkeys && nameMonkey2 in valueMonkeys
    }
}

enum class MonkeyOperator(val sign: String, val executeFor: (Long, Long) -> Long) {
    PLUS("+", { a, b -> a + b }),
    MINUS("-", { a, b -> a - b }),
    MULTIPLY("*", { a, b -> a * b }),
    DIVIDE("/", { a, b -> a / b });

    companion object {
        fun bySign(sign: String) = MonkeyOperator.values().find { it.sign == sign } ?: error("Invalid sign found: $sign")
    }
}

internal fun String.toMathMonkeys(): List<MathMonkey> = mapLines { line ->
    val (name, leftOver) = line.split(": ").take(2)
    when {
        line.length < 14 -> ValueMonkey(name, leftOver.toLong())
        else -> {
            val (nameMonkey1, sign, nameMonkey2) = leftOver.trimIndent().split(" ").take(3)
            OperationMonkey(name, nameMonkey1, nameMonkey2, MonkeyOperator.bySign(sign))
        }
    }
}
