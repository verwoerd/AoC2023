package day22.part2

import day22.part1.readInput
import day22.part1.settleBricks
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 22/12/2023
 */
fun day22Part2(input: BufferedReader): Any {
  val brickSnapshot = input.readInput()
  val (supports, supportedBy) = brickSnapshot.settleBricks()
  return supports.map { (brick, support) ->
    val queue = support.toMutableList()
    val visited = mutableSetOf(brick)
    while (queue.isNotEmpty()) {
      val current = queue.removeFirst()
      if (supportedBy[current]!!.all { it in visited }) {
        visited.add(current)
        queue.addAll(supports[current]!!)
      }
    }
    visited.size - 1
  }.sum()
}
