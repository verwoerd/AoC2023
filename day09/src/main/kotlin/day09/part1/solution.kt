package day09.part1

import extractLongSequence
import java.io.BufferedReader

/**
 * Given a list of sequences, calculate the sum of the next value in the sequence.
 *
 * @author verwoerd
 * @since 09/12/2023
 */
fun day09Part1(input: BufferedReader): Any {
  val sequences = input.lineSequence().extractLongSequence().toList()
  return sequences.sumOf { it.calculateNextLongInSequence() }
}

fun List<Long>.calculateNextLongInSequence(): Long {
  val lastInSequence = last()
  if (all { it == lastInSequence }) {
    return lastInSequence
  }
  val difference = computeDifference()
  val nextInSequence = lastInSequence + difference.calculateNextLongInSequence()
  return nextInSequence
}

fun List<Long>.computeDifference() = zipWithNext { a, b -> b - a }


