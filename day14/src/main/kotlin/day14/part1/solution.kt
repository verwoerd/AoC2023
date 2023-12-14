package day14.part1

import Coordinate
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 14/12/2023
 */
fun day14Part1(input: BufferedReader): Any {
  val (coordinates, boulders) = input.parseBoulderMap()
  val final = coordinates.moveAllNorth(boulders)
  val maxY = coordinates.yRange().second + 1
  return final.sumOf { maxY - it.y }
}

fun MutableMap<Coordinate, Char>.moveAllNorth(boulders: List<Coordinate>) = boulders.map { coordinate ->
  var current = coordinate
  var next = canGoNorth(coordinate)
  while (next != null) {
    this[current] = '.'
    this[next] = 'O'
    current = next
    next = canGoNorth(current)
  }
  current
}

fun Map<Coordinate, Char>.canGoNorth(coordinate: Coordinate) = when {
  coordinate.y == 0 -> null
  get(coordinate.plusY(-1)) == '.' -> coordinate.plusY(-1)
  else -> null
}

fun BufferedReader.parseBoulderMap() = lineSequence().toList()
  .let { rawMap ->
    val coordinates = rawMap.flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Coordinate(x, y) to char
      }
    }
    val boulders = coordinates.filter { (k, v) -> v == 'O' }.map { it.first }
    coordinates.toMap().toMutableMap() to boulders
  }
