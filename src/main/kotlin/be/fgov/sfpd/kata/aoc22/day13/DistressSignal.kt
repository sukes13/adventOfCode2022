package be.fgov.sfpd.kata.aoc22.day13

import be.fgov.sfpd.kata.aoc22.day13.PacketValue.IntPacketValue
import be.fgov.sfpd.kata.aoc22.day13.PacketValue.ListPacketValue
import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String): Int {
    val checks = input.splitOnEmptyLine().map { pairOfPackets ->
        val packets = pairOfPackets.mapLines { it.toPacket() }.windowed(2).map { (first, second) -> first to second }.first()
        packets.first.comesBefore(packets.second)

    }

    return checks.mapIndexed { index, check -> if (check) index else 0 }.sum()
}

fun part2(input: String) = ""

fun String.toPacket() = drop(1).dropLast(1).toPacketValue()

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
        fun comesBefore(other: ListPacketValue): Boolean {
            return values.zip(other.values).all { (packet1, packet2) ->
                if (packet1 is IntPacketValue && packet2 is IntPacketValue) {
                    packet1.comesBefore(packet2) && values.size <= other.values.size//TODO: issue here if right runs out first
                } else if (packet1 is IntPacketValue) {
                    ListPacketValue(listOf(packet1)).comesBefore(packet2 as ListPacketValue) && packet2.values.isNotEmpty()
                } else if (packet2 is IntPacketValue) {
                    (packet1 as ListPacketValue).comesBefore(ListPacketValue(listOf(packet2))) && packet1.values.isNotEmpty()
                } else if (packet1 is ListPacketValue && packet2 is ListPacketValue) {
                    (packet1).comesBefore(packet2) && packet1.values.size <= packet2.values.size
                } else {
                    error("")
                }
            }
        }
    }

    data class IntPacketValue(val value: Int) : PacketValue() {
        fun comesBefore(other: IntPacketValue): Boolean {
            return value <= other.value
        }

    }
}