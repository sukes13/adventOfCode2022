package be.fgov.sfpd.kata.aoc22.day21

import be.fgov.sfpd.kata.aoc22.day21.MathMonkey.OperationMonkey
import be.fgov.sfpd.kata.aoc22.day21.MathMonkey.ValueMonkey
import be.fgov.sfpd.kata.aoc22.day21.MonkeyOperator.MULTIPLY
import be.fgov.sfpd.kata.aoc22.day21.MonkeyOperator.PLUS
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MonkeyMathTest {

    @Test
    fun `parse toMonkeys`() {
        val mathMonkeys = """root: pppw + sjmn
dbpl: 5
cczh: sllz * lgvd""".toMathMonkeys()

        assertThat(mathMonkeys).isEqualTo(listOf(
                OperationMonkey("root", "pppw", "sjmn", PLUS),
                ValueMonkey("dbpl", 5),
                OperationMonkey("cczh", "sllz", "lgvd", MULTIPLY))
        )
    }
}
