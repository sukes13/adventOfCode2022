package be.fgov.sfpd.kata.aoc22.day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class DistressSignalTest {

    @ParameterizedTest(name = "Input:  \"{0}\" vs \"{1}\" in right order: \"{2}\"")
    @MethodSource("testPacketPairs")
    fun `test checkOrder`(packet1: String, packet2: String, result: Int) {
        assertThat(packet1.toPacket().comesBeforeAsList(packet2.toPacket())).isEqualTo(result)
    }

    @ParameterizedTest(name = "Input:  \"{0}\"")
    @MethodSource("testPackets")
    fun `test toPacket`(input: String) {
        assertThat(input.toPacket().toString()).isEqualTo(input)
    }

    companion object {

        @JvmStatic
        fun testPackets() = listOf(
                Arguments.of("[1,1,5,1]"),
                Arguments.of("[[1],4]"),
                Arguments.of("[[1],[2,3,4]]"),
                Arguments.of("[]"),
                Arguments.of("[[[]]]"),
                Arguments.of("[[[[1,2],[5]],6]]"),
                Arguments.of("[[[5]],[1,[]]]"),
                Arguments.of("[[],[]]"),
                Arguments.of("[[[],[]],[]]"),
                Arguments.of("[10]"),
                Arguments.of("[[4,4],4,4]"),
                Arguments.of("[1,[2,[[],[4,[10,6,5]]],[]],9]"),
                Arguments.of("[[[9],[[9,10,7,9,7],[10,0]]],[8],[[[2,4,4,6,9],7,[10,4],3,6],8,[1,10,[0,7,7,7]]],[10,[1,[3,4,7,3,0],4],[[6],10],[[0],8,4,[3],[1,5,6,5]]],[[2,[2,6,10,2,5],6],[4,[7,3,0,9,4],[0],[8]],[10,1],0,5]]"),
                Arguments.of("[[[5],[3,[6,8],[2,9,3],1,[8,8,4,4]],[[1,9],[6,7,6,8]]],[[1],[3,[2],[3]],[[],5,0,[2,8],[9,5,4]]],[7,6,6,10],[5,[5,[9],[2,10,7],10,[2,3]]]]"),
        )

        @JvmStatic
        fun testPacketPairs() = listOf(
                Arguments.of("[1,1,3,1,1]", "[1,1,5,1,1]", 1),
                Arguments.of("[[1],[2,3,4]]", "[[1],4]", 1),
                Arguments.of("[[[1],[2,3,4]]]", "[[[1],4]]", 1),
                Arguments.of("[9]", "[[8,7,6]]", -1),
                Arguments.of("[[4,4],4,4]", "[[4,4],4,4,4]", 1),
                Arguments.of("[[4,4],4,4,[]]", "[[4,4],4,4]", -1),
                Arguments.of("[7,7,7,7]", "[7,7,7]", -1),
                Arguments.of("[]", "[3]", 1),
                Arguments.of("[3]", "[]", -1),
                Arguments.of("[]", "[]", 0),
                Arguments.of("[2,2,4,8,5]", "[6]", 1),
                Arguments.of("[[[]]]", "[[]]", -1),
                Arguments.of("[3,[],[[],[]]]", "[3,[],[[],[],[]]]", 1),
                Arguments.of("[[]]", "[[[]]]", 1),
                Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", "[1,[2,[3,[4,[5,6,0]]]],8,9]", -1),
                Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", "[1,[2,[3,[4,[5,6,9]]]],8,9]", 1),
                Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", "[1,[2,[3,[4,[5,6,7]]]],0,9]", -1),
                Arguments.of("[[[4],[[9,9,4,10,8],[2,2,4,8,5],10,[4,4,10,7,2]],5,7,7]]", "[[0,[[3,3,7],6,2,2],9,[[9,3,1,6],[8,10]],0],[[4,[9,7,2,3]],4,0]]", -1),
                )
    }
}


