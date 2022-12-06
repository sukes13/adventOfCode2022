package be.fgov.sfpd.kata.aoc22.day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TuningTroubleTest {

    @ParameterizedTest(name = "CrateMove:  \"{0}\" of cargo: \"{1}\" results in cargo: \"{2}\"")
    @MethodSource("testSignals4")
    fun `test find first marker`(signal: String, marker: Int) {
        assertThat(signal.firstMaker(4)).isEqualTo(marker)
    }

    companion object {
        @JvmStatic
        fun testSignals4 () = listOf(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 6),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11),
        )

        @JvmStatic
        fun testSignals14 () = listOf(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 23),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 23),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26),
        )
    }
}





