package day21.part1

import Coordinate
import adjacentCoordinates
import printMap
import toCoordinateMap
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 21/12/2023
 */
fun day21Part1(input: BufferedReader, steps: Int = 64): Any {
  val map = input.toCoordinateMap()
  val start = map.filter { it.value == 'S' }.keys.first()
  var locations = listOf(start)
  repeat(steps) {
    locations = locations.flatMap { map.reachable(it) }.distinct()
  }
  return locations.size
}

val cache = mutableMapOf<Coordinate, List<Coordinate>>()
fun Map<Coordinate, Char>.reachable(point: Coordinate) =
  cache.computeIfAbsent(point) {
    adjacentCoordinates(point).filter { get(it) != '#' }.toList()
  }
