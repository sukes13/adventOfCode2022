package be.fgov.sfpd.kata.aoc22.day2

import be.fgov.sfpd.kata.aoc22.day2.RPSResult.*
import be.fgov.sfpd.kata.aoc22.mapLines

fun solution1(input: String) = input.mapLines { it.toRound() }.sumOf { it.score }

fun solution2(input: String) = input.mapLines { it.toPredictedRound() }.sumOf { it.score }

fun String.toRound() =
        splitOnSpace().let { (opponent, player) ->
            Round(opponent.asShape(), player.asShape())
        }

fun String.toPredictedRound() =
        splitOnSpace().let { (opponent, result) ->
            Round(
                    opponentShape = opponent.asShape(),
                    playerShape = opponent.asShape().counterForResult(result.asRPSResult())
            )
        }

data class Round(val opponentShape: Shape, val playerShape: Shape) {
    val score get() = playerShape.value + playerShape.versus(opponentShape).score
}

private fun Shape.versus(opponent: Shape) =
        when (opponent) {
            beats -> WIN
            loosesTo -> LOSS
            else -> DRAW
        }

private fun Shape.counterForResult(expectedResult: RPSResult) =
        when (expectedResult) {
            WIN -> loosesTo
            DRAW -> this
            LOSS -> beats
        }

enum class Shape(val opponentLetter: String, val playerLetter: String, val value: Int) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSORS("C", "Z", 3);
    private val battleCircle get() = values()

    val beats get() = battleCircle.before(this)
    val loosesTo get() = battleCircle.after(this)
}

enum class RPSResult(val letter: String, val score: Int) {
    WIN("Z", 6),
    DRAW("Y", 3),
    LOSS("X", 0);
}

fun String.asRPSResult() = RPSResult.values().single { it.letter == this }
fun String.asShape() = Shape.values().single { it.opponentLetter == this || it.playerLetter == this }

fun String.splitOnSpace(): Pair<String, String> = this.trim().split(" ").zipWithNext().first()

private fun <T> Array<T>.before(element: T) = this.getOrNull(this.indexOf(element) - 1) ?: this.last()
private fun <T> Array<T>.after(element: T) = this.getOrNull(this.indexOf(element) + 1) ?: this.first()