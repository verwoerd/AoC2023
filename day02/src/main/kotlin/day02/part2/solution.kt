package day02.part2

import day02.part1.parseGame
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 02/12/2023
 */
fun day02Part2(input: BufferedReader): Any {
  return input.lineSequence().map { parseGame(it) }.map { it.second }
    .map { list ->
      val maxRed = list.maxOf { it.red }.toLong()
      val maxBlue = list.maxOf { it.blue}.toLong()
      val maxGreen = list.maxOf { it.green }.toLong()
      maxRed*maxBlue*maxGreen
    }.sum()
}
