package day10.part1

import Coordinate
import adjacentCoordinates
import origin
import xRange
import yRange
import java.io.BufferedReader
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

/**
 * @author verwoerd
 * @since 10/12/2023
 */
fun day10Part1(input: BufferedReader): Any {
  val (map, start) = input.readLines().parseMaze()
  val loops = findLoops(map, start)
  return ceil((loops.maxBy { it.size }.size + 1) / 2.0).toLong()
}


// Way to slow, but runs in 8s
fun findLoops(map: Map<Coordinate, Pair<Coordinate, Coordinate>?>, start: Coordinate): MutableList<Set<Coordinate>> {
  val xRange = map.xRange().let { (a, b) -> a..b }
  val yRange = map.yRange().let { (a, b) -> a..b }
  val maze = map.map { (a, b) ->
    when (b) {
      null -> null
      else -> a to b
    }
  }.filterNotNull().filter { (from, to) ->
    to.first.x in xRange && to.first.y in yRange && to.second.x in xRange && to.second.y in yRange
  }.toMap()
  val queue = adjacentCoordinates(start).filter { it in maze }.map { Triple(it, setOf<Coordinate>(), 1) }
    .toCollection(LinkedList())
  var maxLength = 0
  val loops = mutableListOf<Set<Coordinate>>()
  while (queue.isNotEmpty()) {
    val (current, visited, length) = queue.removeFirst()
    if (length < maxLength) continue
    maxLength = max(length, maxLength)
    if (current !in maze) continue
    val (from, to) = maze[current]!!
    val next = when {
      length == 1 && from == start -> to
      length == 1 && to == start -> from
      from !in visited -> from
      else -> to
    }
    if (next == start) loops += visited + current
    queue.add(Triple(next, visited + current, length + 1))
  }
  return loops
}

fun List<String>.parseMaze(): Pair<Map<Coordinate, Pair<Coordinate, Coordinate>?>, Coordinate> {
  var start: Coordinate = origin
  val map = flatMapIndexed { y, line ->
    line.mapIndexed { x, pipe ->
      Coordinate(x, y) to when (pipe) {
        '|' -> Coordinate(x, y - 1) to Coordinate(x, y + 1)
        '-' -> Coordinate(x - 1, y) to Coordinate(x + 1, y)
        'L' -> Coordinate(x, y - 1) to Coordinate(x + 1, y)
        'J' -> Coordinate(x, y - 1) to Coordinate(x - 1, y)
        '7' -> Coordinate(x, y + 1) to Coordinate(x - 1, y)
        'F' -> Coordinate(x, y + 1) to Coordinate(x + 1, y)
        '.' -> null
        'S' -> null.also { start = Coordinate(x, y) }
        else -> error("Invalid input")
      }
    }
  }.toMap()
  return map to start
}
