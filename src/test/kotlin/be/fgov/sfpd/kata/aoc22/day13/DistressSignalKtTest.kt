package be.fgov.sfpd.kata.aoc22.day13

import be.fgov.sfpd.kata.aoc22.day13.PacketValue.IntPacketValue
import be.fgov.sfpd.kata.aoc22.day13.PacketValue.ListPacketValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


class DistressSignalKtTest {

    @ParameterizedTest(name = "Input:  \"{0}\" should give: \"{1}\"")
    @MethodSource("testTrees")
    fun `test toPacket`(input: String, result: PacketValue) {
        assertThat(input.toPacket()).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun testTrees() = listOf(
                Arguments.of("[1,1,5,1]", ListPacketValue(listOf(
                        IntPacketValue(1), IntPacketValue(1), IntPacketValue(5), IntPacketValue(1)))
                ),
                Arguments.of("[[1],4]", ListPacketValue(listOf(
                        ListPacketValue(listOf(IntPacketValue(1))),
                        IntPacketValue(4)))),
                Arguments.of("[[1],[2,3,4]]", ListPacketValue(listOf(
                        ListPacketValue(listOf(
                                IntPacketValue(1))), ListPacketValue(listOf(
                        IntPacketValue(2), IntPacketValue(3), IntPacketValue(4)))))),
                Arguments.of("[]", ListPacketValue()),
                Arguments.of("[[[]]]", ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue()))))),
                Arguments.of("[[[[1,2],[5]],6]]", ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(1),IntPacketValue(2))),ListPacketValue(listOf( IntPacketValue(5))))),IntPacketValue(6)))))),
                Arguments.of("[[[5]],[1,[]]]", ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(5))))),ListPacketValue(listOf(IntPacketValue(1),ListPacketValue()))))),
                Arguments.of("[[],[]]", ListPacketValue(listOf(ListPacketValue(),ListPacketValue()))),
                Arguments.of("[[[],[]],[]]", ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue(), ListPacketValue())), ListPacketValue()))),
                Arguments.of("[10]", ListPacketValue(listOf(
                        IntPacketValue(10)))),
                Arguments.of("[[4,4],4,4]", ListPacketValue(listOf(
                        ListPacketValue(listOf(
                                IntPacketValue(4), IntPacketValue(4))), IntPacketValue(4), IntPacketValue(4)))),
                Arguments.of("[1,[2,[[],[4,[10,6,5]]],[]],9]",
                        ListPacketValue(listOf(IntPacketValue(1), ListPacketValue(listOf(
                                IntPacketValue(2), ListPacketValue(listOf(
                                ListPacketValue(), ListPacketValue(listOf(
                                            IntPacketValue(4), ListPacketValue(listOf(
                                                IntPacketValue(10), IntPacketValue(6), IntPacketValue(5)
                                            )))))),
                              ListPacketValue()
                              )),
                              IntPacketValue(9)
                        ))
                ),
                Arguments.of("[[[9],[[9,10,7,9,7],[10,0]]],[8],[[[2,4,4,6,9],7,[10,4],3,6],8,[1,10,[0,7,7,7]]],[10,[1,[3,4,7,3,0],4],[[6],10],[[0],8,4,[3],[1,5,6,5]]],[[2,[2,6,10,2,5],6],[4,[7,3,0,9,4],[0],[8]],[10,1],0,5]]",
                        ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(value=9))), ListPacketValue(listOf(ListPacketValue(
                                listOf(IntPacketValue(value=9), IntPacketValue(value=10), IntPacketValue(value=7), IntPacketValue(value=9), IntPacketValue(value=7))), ListPacketValue(
                                listOf(IntPacketValue(value=10), IntPacketValue(value=0))))))), ListPacketValue(listOf(IntPacketValue(value=8))),
                            ListPacketValue(listOf(ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(value=2), IntPacketValue(value=4), IntPacketValue(value=4),
                                IntPacketValue(value=6), IntPacketValue(value=9))), IntPacketValue(value=7), ListPacketValue(listOf(IntPacketValue(value=10), IntPacketValue(value=4))),
                                IntPacketValue(value=3), IntPacketValue(value=6))), IntPacketValue(value=8), ListPacketValue(listOf(IntPacketValue(value=1), IntPacketValue(value=10),
                                ListPacketValue(listOf(IntPacketValue(value=0), IntPacketValue(value=7), IntPacketValue(value=7), IntPacketValue(value=7))))))),
                            ListPacketValue(listOf(IntPacketValue(value=10), ListPacketValue(listOf(IntPacketValue(value=1), ListPacketValue(listOf(IntPacketValue(value=3),
                                IntPacketValue(value=4), IntPacketValue(value=7), IntPacketValue(value=3), IntPacketValue(value=0))), IntPacketValue(value=4))),
                                ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(value=6))), IntPacketValue(value=10))),
                                ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(value=0))), IntPacketValue(value=8), IntPacketValue(value=4),
                                    ListPacketValue(listOf(IntPacketValue(value=3))), ListPacketValue(listOf(IntPacketValue(value=1), IntPacketValue(value=5), IntPacketValue(value=6),
                                        IntPacketValue(value=5))))))), ListPacketValue(listOf(ListPacketValue(listOf(IntPacketValue(value=2), ListPacketValue(listOf(IntPacketValue(value=2),
                                IntPacketValue(value=6), IntPacketValue(value=10), IntPacketValue(value=2), IntPacketValue(value=5))), IntPacketValue(value=6))),
                                ListPacketValue(listOf(IntPacketValue(value=4), ListPacketValue(listOf(IntPacketValue(value=7), IntPacketValue(value=3), IntPacketValue(value=0),
                                    IntPacketValue(value=9), IntPacketValue(value=4))), ListPacketValue(listOf(IntPacketValue(value=0))), ListPacketValue(listOf(IntPacketValue(value=8))))),
                                ListPacketValue(listOf(IntPacketValue(value=10), IntPacketValue(value=1))), IntPacketValue(value=0), IntPacketValue(value=5))))),
        ))
    }

}


