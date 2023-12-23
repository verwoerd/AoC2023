package day23.part1

import Coordinate
import FourDirectionFlipped
import priorityQueueOf
import toCoordinateMap
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 23/12/2023
 */
fun day23Part1(input: BufferedReader): Any {
  val map = input.toCoordinateMap()
  val xRange = map.xRange().let { (a, b) -> a..b }
  val yRange = map.yRange().let { (a, b) -> a..b }
  val queue = priorityQueueOf(Comparator.comparing { it: Triple<Coordinate, Long, Set<Coordinate>> -> it.second })
  val start = map.filter { it.key.y == yRange.first }.filter { it.value == '.' }.keys.first()
  val target = map.filter { it.key.y == yRange.last }.filter { it.value == '.' }.keys.first()
  queue.add(Triple(start, 0L, setOf(start)))
  val visited = mutableMapOf<Coordinate, Long>()
  var best = 0L
  while (queue.isNotEmpty()) {
    val (current, length, path) = queue.poll()
    if (current == target) {
      best = best.coerceAtLeast(length)
    }
    if (current in visited && visited[current]!! > length) continue
    visited[current] = length
    FourDirectionFlipped.entries
      .asSequence()
      .map { it to it + current }
      .filter { (direction, coordinate) -> coordinate.x in xRange && coordinate.y in yRange }
      .filter { (_, coordinate) -> coordinate !in path }
      .filter { (direction, coordinate) -> map[coordinate]!! != '#' && map[coordinate]!! != direction.uphill() }
      .map { (_, it) -> Triple(it, length + 1, path + it) }
      .toCollection(queue)
  }
  return best
}

fun FourDirectionFlipped.uphill() = when (this) {
  FourDirectionFlipped.DOWN -> '^'
  FourDirectionFlipped.UP -> 'v'
  FourDirectionFlipped.LEFT -> '>'
  FourDirectionFlipped.RIGHT -> '<'
}
