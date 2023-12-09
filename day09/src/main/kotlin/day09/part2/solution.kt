package day09.part2

import day09.part1.computeDifference
import extractLongSequence
import java.io.BufferedReader

/**
 * Given a list of sequences, calculate the sum of the previous elements in the sequence.
 *
 * @author verwoerd
 * @since 09/12/2023
 */
fun day09Part2(input: BufferedReader): Any {
  val inputs = input.lineSequence().extractLongSequence().toList()
  return inputs.sumOf { it.calculatePreviousLongInSequence() }
}

fun List<Long>.calculatePreviousLongInSequence(): Long {
  val firstInSequence = first()
  if (all { it == firstInSequence }) {
    return firstInSequence
  }
  val difference = computeDifference()
  val previousInSequence = firstInSequence - difference.calculatePreviousLongInSequence()
  return previousInSequence
}

