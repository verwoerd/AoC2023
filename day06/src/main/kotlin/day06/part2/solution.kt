package day06.part2

import day06.part1.calculateOptions
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 06/12/2023
 */
fun day06Part2(input: BufferedReader): Any {
  val data = input.parseInput()
  return calculateOptions(data)
}

fun BufferedReader.parseInput(): Pair<Long, Long> {
  val lines = readLines()
  val times = lines[0].split(":", limit = 2)[1].filter { it.isDigit() }.toLong()
  val distance = lines[1].split(":", limit = 2)[1].filter { it.isDigit() }.toLong()
  return times to distance
}
