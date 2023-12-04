package day04.part2

import day04.part1.parseLine
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 04/12/2023
 */
fun day04Part2(input: BufferedReader): Any {
  val cards = input.lineSequence().map { parseLine(it) }.toList()
  val cardLookUp = cards.associateBy { it.number }
  var sum = cards.size
  var current = cards
  while (current.isNotEmpty()) {
    current = current.flatMap { card ->
      card.calculateWonCards().map { cardLookUp[it]!! }.also { sum += it.size }
    }
  }
  return sum
}
