package be.fgov.sfpd.kata.aoc22.day13

import be.fgov.sfpd.kata.aoc22.day13.PacketValue.ArrayPacketValue
import be.fgov.sfpd.kata.aoc22.day13.PacketValue.IntPacketValue
import be.fgov.sfpd.kata.aoc22.mapLines
import be.fgov.sfpd.kata.aoc22.splitOnEmptyLine

fun part1(input: String): Int {
    val checks = input.splitOnEmptyLine().map { pairOfPackets ->
        val packets = pairOfPackets.mapLines { it.toPacket() }.windowed(2).map { (first, second) -> first to second }.first()
        packets.first.comesBefore(packets.second)
    }

    return checks.mapIndexed { index, check -> if (check == 1) index + 1 else 0 }.sum()
}

fun part2(input: String): Int {
    val dividerPackets = listOf("[[2]]".toPacket(), "[[6]]".toPacket())
    val allPackets = (input.replace("\r\n\r\n", "\r\n")).mapLines { it.toPacket() } + dividerPackets

    return allPackets.sortedWith { packet1, packet2 -> packet2.comesBefore(packet1) }.mapIndexed { index, packet ->
        if (packet in dividerPackets) index + 1 else 1
    }.reduce { a, b -> a * b }
}

sealed class PacketValue {

    data class ArrayPacketValue(val values: List<PacketValue> = listOf()) : PacketValue() {
        fun comesBefore(other: ArrayPacketValue): Int {
            val longestListSize = if (values.size <= other.values.size) other.values.size else values.size
            var match = 0
            var index = 0
            while (match == 0 && index < longestListSize) {
                val packet1 = values.getOrNull(index)
                val packet2 = other.values.getOrNull(index)
                when {
                    packet1 == null -> {
                        match = 1.also { println("-> in order because: left ran out first") }
                    }
                    packet2 == null -> {
                        match = (-1).also { println("-> NOT in order because: right ran out first") }
                    }
                    packet1 is ArrayPacketValue && packet2 is ArrayPacketValue -> {
                        match += packet1.comesBefore(packet2)
                    }
                    packet1 is IntPacketValue && packet2 is ArrayPacketValue -> {
                        match += ArrayPacketValue(listOf(packet1)).comesBefore(packet2)
                    }
                    packet2 is IntPacketValue && packet1 is ArrayPacketValue -> {
                        match += packet1.comesBefore(ArrayPacketValue(listOf(packet2)))
                    }
                    packet1 is IntPacketValue && packet2 is IntPacketValue -> {
                        when {
                            packet1 < packet2 -> match = 1.also { println("-> in order because: $packet1 < $packet2") }
                            packet1 > packet2 -> match = (-1).also { println("-> NOT in order because: $packet1 > $packet2") }
                        }
                    }
                }
                index += 1
            }
            return match
        }

        override fun toString() = "[${values.joinToString(",")}]"
        operator fun plus(entireList: PacketValue) = copy(values = values.toMutableList() + entireList)
        fun fillWith(entireList: List<PacketValue>) = copy(values = values.toMutableList() + entireList)

    }

    data class IntPacketValue(val value: Int) : PacketValue() {
        override fun toString() = "$value"
        operator fun compareTo(other: IntPacketValue) = this.value.compareTo(other.value)
    }
}

fun String.toPacket() = drop(1).dropLast(1).toPacketValue()

fun String.toPacketValue(): ArrayPacketValue {
    var stringToCheck = this
    var packet = ArrayPacketValue()

    while (stringToCheck.isNotEmpty()) {
        when {
            stringToCheck.first() == '[' -> {
                val entireList = stringToCheck.findNextArrayContent()
                packet += entireList.toPacketValue()
                stringToCheck = stringToCheck.drop(entireList.length + 1)
            }

            stringToCheck.first().isDigit() -> {
                val allValues = stringToCheck.substringBefore('[').substringBefore(']')
                packet = packet.fillWith(allValues.toIntPackages())
                stringToCheck = stringToCheck.drop(allValues.length)
            }

            stringToCheck.first() == ']' || stringToCheck.first() == ',' -> {
                stringToCheck = stringToCheck.drop(1)
            }
        }
    }

    return packet
}

private fun String.findNextArrayContent(): String {
    var content = substringBefore(']')
    (0..6).forEach { _ ->
        content = run {
            val dept = content.count { it == '[' }
            this.positionOfCloserFor(dept)?.let { correctedCloser ->
                this.take(correctedCloser)
            } ?: content.drop(1)
        }
    }
    return content.drop(1)
}

private fun String.positionOfCloserFor(dept: Int) = Regex("]").findAll(this).map { it.range.first }.toList().getOrNull(dept - 1)

private fun String.toIntPackages() = split(",").filter { it != "" }.map { it.toInt() }.map { IntPacketValue(it) }

