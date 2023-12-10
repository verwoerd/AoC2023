package day10.part2

import Coordinate
import day10.part1.findLoops
import day10.part1.parseMaze
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 10/12/2023
 */
fun day10Part2(input: BufferedReader): Any {
  val rawMaze = input.lineSequence().toList()
  val (map, start) = rawMaze.parseMaze()
  val loop = findLoops(map, start).maxBy { it.size } + start
  val first = loop.first()
  val last = loop.last()

  fun Coordinate.goesUp() = when (rawMaze[y][x]) {
    '|', 'L', 'J' -> true
    else -> false
  }

  val startGoesUp = first.y < start.y || last.y < start.y


  val northConnections = loop.filter { current ->
    current.goesUp()
  }.toSet() + if (startGoesUp) setOf(start) else emptySet<Coordinate>()
  var inside = 0L
  rawMaze.forEachIndexed { y, line ->
    var parity = 0
    line.indices.forEach { x ->
      if (Coordinate(x, y) !in loop) {
        if (parity % 2 == 1) {
          inside++
        }
      } else {
        if (northConnections.contains(Coordinate(x, y))) parity++
      }
    }
  }
  return inside
}


