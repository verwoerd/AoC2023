package day08.part1

import day08.part1.Direction.Companion.toDirection
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 08/12/2023
 */
fun day08Part1(input: BufferedReader): Any {
  val directionsMap = input.parseInput()
  val route = mutableListOf("AAA")
  return generateSequence { directionsMap.directions }.flatten().withIndex().map { (index, direction) ->
    addDirectionToRoute(route, index, direction, directionsMap.map)
    index
  }.first { index -> route[index] == "ZZZ" }
}

fun addDirectionToRoute(
  route: MutableList<String>, index: Int, direction: Direction, map: Map<String, Pair<String, String>>
) {
  val directionKey = map[route[index]]!!
  val newDirection = direction.go(directionKey)
  route.add(index + 1, newDirection)
}

enum class Direction(val go: (Pair<String, String>) -> String) {
  LEFT({ (a, _) -> a }), RIGHT({ (a, b) -> b });

  companion object {
    fun Char.toDirection(): Direction = when (this) {
      'L' -> LEFT
      'R' -> RIGHT
      else -> error("Invalid char to convert to Direction: $this")
    }
  }
}

data class DirectionMap(
  val directions: List<Direction>, val map: Map<String, Pair<String, String>>
)

val regex = Regex("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)")
fun BufferedReader.parseInput(): DirectionMap {
  val directions = parseDirections()
  val map = createDirectionMap()
  return DirectionMap(directions, map)
}

fun BufferedReader.parseDirections() = readLine().asSequence().map { it.toDirection() }.toList()

fun BufferedReader.createDirectionMap(): Map<String, Pair<String, String>> {
  return lineSequence().drop(1)
    .map { line -> regex.matchEntire(line)!!.destructured.let { (a, b, c) -> a to (b to c) } }.toMap()
}
