package day04.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 04/12/2023
 */
fun day04Part1(input: BufferedReader): Any {
  return input.lineSequence().map { parseLine(it) }.sumOf { it.calculateScore() }
}

fun parseLine(line: String): ScratchCard {
  val (card, rest) = line.split(": ", limit = 2)
  val (winning, potential) = rest.split(" | ", limit = 2)
  return ScratchCard(
    card.drop(4).trim().toInt(),
    winning.split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() },
    potential.split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() })
}

data class ScratchCard(
  val number: Int,
  val winningNumbers: List<Int>,
  val numbersOnCard: List<Int>
) {
  private val wins: Int by lazy { numbersOnCard.count { it in winningNumbers } }
  fun calculateScore(): Int {
    if (wins == 0) return 0
    return 1 shl (wins - 1)
  }

  fun calculateWonCards() = when (wins) {
    0 -> emptyList()
    else -> (number + 1..number + wins).toList()
  }
}
