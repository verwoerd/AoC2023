package day17.part2

import day17.part1.findPath
import toIntValue
import toSpecializedCoordinateMap
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 17/12/2023
 */
fun day17Part2(input: BufferedReader): Any {
  val map = input.toSpecializedCoordinateMap { it.toIntValue() }
  return map.findPath(10, 4, minMovedForEnd = 4)
}

