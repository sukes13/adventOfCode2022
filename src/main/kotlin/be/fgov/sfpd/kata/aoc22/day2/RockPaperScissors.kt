package be.fgov.sfpd.kata.aoc22.day2

import be.fgov.sfpd.kata.aoc22.day2.RPSResult.*
import be.fgov.sfpd.kata.aoc22.day2.Shape.*

fun solution1(input: String) = input.toRounds().sumOf { it.score() }

fun solution2(input: String): Int = TODO()


data class Round(val opponent: Shape, val player: Shape) {
    fun score(): Int {
        val shapeScore = this.player.score
        val result = this.player.battle(this.opponent)

        return shapeScore + result.score
    }
}

fun Shape.battle(opponent: Shape) =
    when (this) {
        ROCK -> when (opponent) {
            PAPER -> LOSS
            ROCK -> DRAW
            SCISSORS -> WIN
        }
        PAPER -> when (opponent) {
            PAPER -> DRAW
            ROCK -> WIN
            SCISSORS -> LOSS
        }
        SCISSORS -> when (opponent) {
            PAPER -> WIN
            ROCK -> LOSS
            SCISSORS -> DRAW
        }
    }

enum class Shape(val opponentLetter: String, val playerLetter: String, val score: Int) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSORS("C", "Z", 3);
}

enum class RPSResult(val score: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0)
}

fun String.asShape() = Shape.values().single { it.opponentLetter == this || it.playerLetter == this }
fun String.toRounds() = this.lines().map { it.toRound() }
fun String.toRound() = this.trim().split(" ").let { (opponent, player) -> Round(opponent.asShape(), player.asShape()) }
