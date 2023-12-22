package day21.part2

import Coordinate
import adjacentCoordinates
import toCoordinateMap
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 21/12/2023
 */
fun day21Part2(input: BufferedReader, steps: Int = 26501365): Any {
  val map = input.toCoordinateMap()
  val start = map.filter { it.value == 'S' }.keys.first()
  xRange = map.xRange().let { (a, b) -> a..b }
  xMax = xRange.last + 1
  yRange = map.xRange().let { (a, b) -> a..b }
  modCoordinate = Coordinate(map.xRange().second + 1, map.yRange().second + 1)
  var widthDistance = 0
  var firstWidthDistance = 0
  var index = 0
  var queue = listOf(start)
  val history = mutableListOf(queue.size)
  while (index < steps) {
    if (firstWidthDistance > 0 && index == firstWidthDistance + xMax) break
    queue = queue.flatMap { map.reachable(it) }.distinct()
    history.add(queue.size)
    if (firstWidthDistance==0 && index > 3 * xMax) {
      val widthDiff = history[index] - history[index - xMax]
      val secondWithDiff = history[index - xMax] - history[index - 2 * xMax]
      val thirdWithDiff = history[index - 2 * xMax] - history[index - 3 * xMax]
      if (widthDiff - secondWithDiff == secondWithDiff - thirdWithDiff) {
        widthDistance = widthDiff - secondWithDiff
        firstWidthDistance = index
      }
    }
    index++
  }
  val offset = (steps - firstWidthDistance) % xMax
  var startIndex = firstWidthDistance + offset
  var result = history[startIndex].toLong()
  var iterationDiff = 0L + history[startIndex] - history[startIndex - xMax]
  while (startIndex != steps) {
    iterationDiff += widthDistance
    startIndex += xMax
    result += iterationDiff
  }
  return result
}


lateinit var xRange: IntRange
lateinit var yRange: IntRange
var xMax: Int = 0
lateinit var modCoordinate: Coordinate
val cache = mutableMapOf<Coordinate, List<Coordinate>>()
fun Map<Coordinate, Char>.reachable(point: Coordinate) =
  cache.computeIfAbsent(point) {
    adjacentCoordinates(point).filter { get(it.mod(modCoordinate)) != '#' }.toList()
  }


