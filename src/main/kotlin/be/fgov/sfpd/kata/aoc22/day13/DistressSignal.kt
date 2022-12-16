package be.fgov.sfpd.kata.aoc22.day13

import be.fgov.sfpd.kata.aoc22.day13.PacketValue.IntPacketValue
import be.fgov.sfpd.kata.aoc22.day13.PacketValue.ListPacketValue
import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String): Int {
    val checks = input.splitOnEmptyLine().map { pairOfPackets ->
        val packets = pairOfPackets.mapLines { it.toPacket() }.windowed(2).map { (first, second) -> first to second }.first()
        packets.first.comesBeforeAsList(packets.second)
    }

    return checks.mapIndexed { index, check -> if (check==1) index + 1 else 0 }.sum()
}

fun part2(input: String) = ""
sealed class PacketValue {
    data class ListPacketValue(val values: List<PacketValue> = listOf()) : PacketValue() {
        fun comesBeforeAsList(other: ListPacketValue): Int {
            println("comparing lists: $this vs $other")
            val longestListSize = if (values.size <= other.values.size) other.values.size else values.size
            var match = 0
            var index = 0
            while (match == 0 && index < longestListSize){
                val packet1 = values.getOrNull(index)
                val packet2 = other.values.getOrNull(index)
                when {
                    packet1 == null -> {
                        match = 1.also { println("-> in order because: left ran out first") }
                    }
                    packet2 == null -> {
                        match =(-1).also { println("-> NOT in order because: right ran out first") }
                    }
                    packet1 is ListPacketValue && packet2 is ListPacketValue -> {
                        match += packet1.comesBeforeAsList(packet2)
                    }
                    packet1 is IntPacketValue && packet2 is ListPacketValue -> {
                        match += ListPacketValue(listOf(packet1)).comesBeforeAsList(packet2)
                    }
                    packet2 is IntPacketValue && packet1 is ListPacketValue-> {
                        match += packet1.comesBeforeAsList(ListPacketValue(listOf(packet2)))
                    }
                    packet1 is IntPacketValue && packet2 is IntPacketValue -> {
                        println("comparing values: $packet1 vs $packet2")
                        if(packet1 < packet2) match = 1.also { println("-> in order because: $packet1 < $packet2") }
                        if(packet1 > packet2) match = (-1).also { println("-> NOT in order because: $packet1 > $packet2") }
                    }
                }
                index += 1
            }
            return match
        }

        override fun toString() = "[${values.joinToString (",") }]"

    }

    data class IntPacketValue(val value: Int) : PacketValue() {
        override fun toString() = "$value"
        operator fun compareTo(other: IntPacketValue) = this.value.compareTo(other.value)
    }
}

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

