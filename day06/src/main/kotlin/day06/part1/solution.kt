package day06.part1

import java.io.BufferedReader
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * @author verwoerd
 * @since 06/12/2023
 */
fun day06Part1(input: BufferedReader): Any {
  val targets = input.parseInput()
  return targets.map { calculateOptions(it) }.fold(1L) { acc, pairs -> acc * pairs }
}

fun BufferedReader.parseInput(): List<Pair<Long, Long>> {
  val lines = readLines()
  val times = lines[0].split(":", limit = 2)[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
  val distance = lines[1].split(":", limit = 2)[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
  return times.zip(distance)
}

fun calculateOptions(target: Pair<Long, Long>): Long {
  val (time, distance) = target
  // (time-x)x > distance
  // -x^2 + time*x - distance > 0
  val left = floor((-time + sqrt((time * time - 4 * distance).toDouble())) / -2).toLong() + 1
  val right = ceil((-time - sqrt((time * time - 4 * distance).toDouble())) / -2).toLong() - 1
  return right - left + 1
}
