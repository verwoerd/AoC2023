package day17.part1

import Coordinate
import FourDirectionFlipped
import manhattanDistance
import origin
import priorityQueueOf
import toIntValue
import toSpecializedCoordinateMap
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 17/12/2023
 */
fun day17Part1(input: BufferedReader): Any {
  val map = input.toSpecializedCoordinateMap { it.toIntValue() }
  return map.findPath(3)
}

data class History(
  val lastDirection: FourDirectionFlipped, val location: Coordinate, val count: Int
) {
  fun moveDirections(maxConsecutive: Int, minConsecutive: Int): List<History> {
    if (count == 0) {
      return listOf(FourDirectionFlipped.RIGHT, FourDirectionFlipped.DOWN).map { History(it, it + location, 1) }
    }
    val leftRight = listOf(lastDirection.turnLeft(), lastDirection.turnRight()).map { History(it, it + location, 1) }
    val forward = History(lastDirection, lastDirection + location, count + 1)
    if (count < minConsecutive) {
      return listOf(forward)
    }
    if (count == maxConsecutive) {
      return leftRight
    }
    return leftRight + forward
  }
}

fun Map<Coordinate, Int>.findPath(
  maxConsecutive: Int, minConsecutive: Int = 0, minMovedForEnd: Int = 0
): Long {
  val xRange = xRange().let { (a, b) -> a..b }
  val yRange = yRange().let { (a, b) -> a..b }
  val target = Coordinate(xRange.last, yRange.last)
  val queue = priorityQueueOf(
    Comparator.comparing { -manhattanDistance(it.first.location, target) },
    History(FourDirectionFlipped.RIGHT, origin, 0) to 0L
  )
  val visited = mutableMapOf<History, Long>()
  var min = Long.MAX_VALUE
  while (queue.isNotEmpty()) {
    val (history, loss) = queue.poll()
    if (loss > (visited[history] ?: Long.MAX_VALUE)) continue
    if (history.location == target && history.count >= minMovedForEnd) {
      min = min.coerceAtMost(loss)
      continue
    }
    history.moveDirections(maxConsecutive, minConsecutive)
      .asSequence()
      .filter { it.location.x in xRange && it.location.y in yRange }
      .map { it to loss + get(it.location)!! }
      .filter { it.second < (visited[it.first] ?: Long.MAX_VALUE) }
      .onEach { visited[it.first] = it.second }
      .toCollection(queue)
  }
  return min

}
