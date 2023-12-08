package day08.part2

import day08.part1.Direction
import day08.part1.DirectionMap
import day08.part1.parseInput
import lcmForList
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 08/12/2023
 */
fun day08Part2(input: BufferedReader): Any {
  val directionMap = input.parseInput()
  val targetNodes = directionMap.map.keys.filter { it.endsWith("A") }
  val stepsToEndNode = targetNodes.map { calculateStepsToEnd(directionMap, it, "Z") }
  return stepsToEndNode.lcmForList()
}

fun calculateStepsToEnd(map: DirectionMap, start: String, end: String): Long {
  val route = mutableListOf(start)
  return generateSequence { map.directions }.flatten()
    .withIndex().map { (index, direction) ->
      route.addStep(index, direction, map.map[route[index]]!!)
      index
    }
    .first { index -> route[index].endsWith(end) }.toLong()
}

fun MutableList<String>.addStep(index: Int, direction: Direction, step: Pair<String, String>) {
  add(index + 1, direction.go(step))
}

