package day11.part2

import day11.part1.distanceTo
import day11.part1.readGalaxiesAndExpand
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 11/12/2023
 */

fun day11Part2(input: BufferedReader, factor: Int = 1000000): Any {
  val stars = input.readGalaxiesAndExpand(factor)
  return stars.flatMapIndexed { index, star ->
    stars.drop(index + 1).map { star to it }
  }.sumOf { (a, b) -> a.distanceTo(b) }
}
