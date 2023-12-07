package day07.part2

import day07.part1.findHandType
import day07.part1.readInput
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 07/12/2023
 */
fun day07Part2(input: BufferedReader): Any {
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

  private fun calculateTypeValue(): Int {
    val a = hand.groupBy { it }.mapValues { it.value.size }.toMutableMap()
    val jokers = a['J'] ?: 0
    if (jokers == 5) return 7
    a.remove('J')
    val max = a.maxBy { it.value }
    a[max.key] = a[max.key]!! + jokers
    return a.findHandType()
  }
}

val order = mapOf(
  'J' to 0,
  '2' to 1,
  '3' to 2,
  '4' to 3,
  '5' to 4,
  '6' to 5,
  '7' to 6,
  '8' to 7,
  '9' to 8,
  'T' to 9,
  'Q' to 10,
  'K' to 11,
  'A' to 12
)
