package day19.part2

import day19.part1.Condition
import day19.part1.readInput
import linkedListOf
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 19/12/2023
 */
fun day19Part2(input: BufferedReader): Any {
  val (rules) = input.readInput()
  val results = mutableListOf<RangeAssignment>()
  val queue = linkedListOf(RangeAssignment() to "in")
  while (queue.isNotEmpty()) {
    val (current, label) = queue.removeFirst()
    if (label == "A") results.add(current)
    if (label == "A" || label == "R") continue
    val rule = rules[label]!!
    val result = rule.decissions.runningFold(Triple(current, current, label)) { (_, falseValue), condition ->
      falseValue.split(condition).let { (a, b) -> Triple(a, b, condition.successLabel) }
    }.drop(1)
    result.map { it.first to it.third }.filter { it.first.isValid() }.toCollection(queue)
    if (result.last().second.isValid()) {
      queue.add(result.last().second to rule.elseRule)
    }
  }
  return results.sumOf { it.sum() }
}

data class RangeAssignment(
  val xMin: Long = 1,
  val xMax: Long = 4000,
  val mMin: Long = 1,
  val mMax: Long = 4000,
  val aMin: Long = 1,
  val aMax: Long = 4000,
  val sMin: Long = 1,
  val sMax: Long = 4000
) {
  fun split(condition: Condition): Pair<RangeAssignment, RangeAssignment> {
    return when (condition.field) {
      'x' -> when (condition.lessThen) {
        true -> copy(xMax = condition.value - 1) to copy(xMin = condition.value)
        else -> copy(xMin = condition.value + 1) to copy(xMax = condition.value)
      }

      'm' -> when (condition.lessThen) {
        true -> copy(mMax = condition.value - 1) to copy(mMin = condition.value)
        else -> copy(mMin = condition.value + 1) to copy(mMax = condition.value)
      }

      'a' -> when (condition.lessThen) {
        true -> copy(aMax = condition.value - 1) to copy(aMin = condition.value)
        else -> copy(aMin = condition.value + 1) to copy(aMax = condition.value)
      }

      's' -> when (condition.lessThen) {
        true -> copy(sMax = condition.value - 1) to copy(sMin = condition.value)
        else -> copy(sMin = condition.value + 1) to copy(sMax = condition.value)
      }

      else -> error("Inlvaid")
    }
  }

  fun isValid() = xMin <= xMax && mMin <= mMax && aMin <= aMax && sMin <= sMax

  fun sum() = (xMax - xMin + 1) * (mMax - mMin + 1) * (aMax - aMin + 1) * (sMax - sMin + 1)
}

