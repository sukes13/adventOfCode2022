package be.fgov.sfpd.kata.aoc22.day13

import be.fgov.sfpd.kata.aoc22.day13.PacketValue.IntPacketValue
import be.fgov.sfpd.kata.aoc22.day13.PacketValue.ListPacketValue

fun part1(input: String) = ""

fun part2(input: String) = ""

fun String.toPacket(): ListPacketValue = drop(1).dropLast(1).toPacketValue()

fun String.toPacketValue(): ListPacketValue {
    var stringToCheck = this
    var listPacketValue = ListPacketValue()

    while (stringToCheck.isNotEmpty()) {
        when {
            stringToCheck.first() == '[' -> {
                val entireList = stringToCheck.findNextListContent()
                listPacketValue = listPacketValue.copy(values = listPacketValue.values.toMutableList() + entireList.toPacketValue())
                stringToCheck = stringToCheck.drop(entireList.length + 1)
            }

            stringToCheck.first().isDigit() -> {
                val allValues = stringToCheck.substringBefore('[').substringBefore(']')
                val packetValuesToAdd = allValues.toIntPackages()
                listPacketValue = listPacketValue.copy(values = listPacketValue.values.toMutableList() + packetValuesToAdd)
                stringToCheck = stringToCheck.drop(allValues.length)
            }

            stringToCheck.first() == ']' || stringToCheck.first() == ',' -> {
                stringToCheck = stringToCheck.drop(1)
            }
        }
    }

    return listPacketValue
}

private fun String.toIntPackages() = split(",").filter { it != "" }.map { it.toInt() }.map { IntPacketValue(it) }

private fun String.findNextListContent(): String {
    var listContent = substringBefore(']')
    (0..6).forEach { _ ->
        listContent = run {
            val deptInList = listContent.count { it == '[' }
            this.positionOfCloserFor(deptInList)?.let { correctedCloser ->
                this.take(correctedCloser)
            } ?: listContent.drop(1)
        }
    }
    return listContent.drop(1)
}

private fun String.positionOfCloserFor(dept: Int) =
        Regex("]").findAll(this).map { it.range.first }.toList().getOrNull(dept - 1)

sealed class PacketValue {
    data class ListPacketValue(val values: List<PacketValue> = listOf()) : PacketValue() {

    }

    data class IntPacketValue(val value: Int) : PacketValue() {

    }


}