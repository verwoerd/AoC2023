package day11.part1

import Coordinate
import adjacentCoordinates
import java.io.BufferedReader
import java.util.*
import kotlin.math.abs

/**
 * @author verwoerd
 * @since 11/12/2023
 */
fun day11Part1(input: BufferedReader): Any {
  val stars = input.readGalaxiesAndExpand(2)
  return stars.flatMapIndexed { index, star ->
    stars.drop(index + 1).map { star to it }
  }.sumOf { (a, b) -> a.distanceTo(b) }
}

fun Coordinate.distanceTo(other: Coordinate): Long {

  val xDiff = abs(x - other.x).toLong()
  val yDiff = abs(y - other.y).toLong()
  val distance = when {
    x == other.x -> yDiff
    y == other.y -> xDiff
    xDiff == yDiff -> 2 * xDiff
    xDiff > yDiff -> 2 * yDiff + (xDiff - yDiff)
    else -> 2 * xDiff + (yDiff - xDiff)
  }
  // check for finding the off by one error 😱
//  val realDistance = dijkstraCoordinate(this, other)
//  if (distance - realDistance != 0L) {
//    error("Found issue in distance between $this and $other, calculated $distance, but should be $realDistance")
//  }
  return distance
}

fun dijkstraCoordinate(from: Coordinate, to: Coordinate): Int {
  val visited = mutableSetOf(from)
  val xRange = if (from.x < to.x) from.x..to.x else to.x..from.x
  val yRange = if (from.y < to.y) from.y..to.y else to.y..from.y
  val queue = PriorityQueue<Pair<Coordinate, Int>> { a, b -> a.second.compareTo(b.second) }
  queue.add(from to 0)
  while (queue.isNotEmpty()) {
    val (current, length) = queue.remove()
    if (current == to) {
      return length
    }
    visited.add(current)
    adjacentCoordinates(current).filter { it.x in xRange && it.y in yRange }.filter { it !in visited }
      .map { it to length + 1 }.toCollection(queue)
  }

  error(" No result found")
}

fun BufferedReader.readGalaxiesAndExpand(increaseFactor: Int = 1): List<Coordinate> {
  val rawMap = readLines()
  val blankLines = rawMap.withIndex().filter { (_, line) -> line.all { it == '.' } }.map { it.index }.reversed()
  val blankColumns = rawMap.first().indices.filter { x -> rawMap.all { it[x] == '.' } }.reversed()
  val stars = rawMap.flatMapIndexed { y, row ->
    row.withIndex().filter { it.value == '#' }.map { (x) -> Coordinate(x, y) }
  }
  return stars.map { (x, y) ->
    Coordinate(x + blankColumns.count { it < x } * increaseFactor, y + blankLines.count { it < y } * increaseFactor)
  }
}
