package day07.part1

import java.io.BufferedReader
import kotlin.math.pow

/**
 * @author verwoerd
 * @since 07/12/2023
 */
fun day07Part1(input: BufferedReader): Any {
  return input.readInput(order).sorted().withIndex().fold(0L) { acc, (indexedValue, hand) ->
    acc + hand.bid * (indexedValue + 1)
  }
}

data class Hand(
  val hand: List<Char>, val value: Long, val bid: Long
) : Comparable<Hand> {
  private val typeValue: Int by lazy { this.calculateTypeValue() }

  override fun compareTo(other: Hand): Int = when (val type = this.typeValue.compareTo(other.typeValue)) {
    0 -> this.value.compareTo(other.value)
    else -> type
  }

  private fun calculateTypeValue(): Int = hand.groupBy { it }.mapValues { it.value.size }.findHandType()
}

val order = mapOf(
  '2' to 0,
  '3' to 1,
  '4' to 2,
  '5' to 3,
  '6' to 4,
  '7' to 5,
  '8' to 6,
  '9' to 7,
  'T' to 8,
  'J' to 9,
  'Q' to 10,
  'K' to 11,
  'A' to 12
)


fun BufferedReader.readInput(orderMapping: Map<Char, Int>) = lineSequence().map { line ->
  line.split(" ", limit = 2).let { (hand, bid) ->
    val value = hand.reversed().withIndex()
      .fold(0L) { acc, (index, card) -> acc + (orderMapping[card]!! * 13.0.pow(index)).toLong() }
    Hand(hand.toList(), value, bid.toLong())
  }
}.toList()

fun Map<Char, Int>.findHandType() = when {
  containsValue(5) -> 7
  containsValue(4) -> 6
  containsValue(3) && containsValue(2) -> 5
  containsValue(3) -> 4
  size == 3 && count { it.value == 2 } == 2 -> 3
  containsValue(2) -> 2
  else -> 1
}
