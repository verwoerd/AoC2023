package day14.part2

import Coordinate
import FourDirections
import day14.part1.parseBoulderMap
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 14/12/2023
 */
fun day14Part2(input: BufferedReader): Any {
  val (coordinates, boulders) = input.parseBoulderMap()
  var currentBoulders = boulders

  val maxY = coordinates.yRange().second + 1
  var count = 0
  val seen = mutableSetOf(boulders to coordinates.toMap())
  val yLoad = mutableListOf<Int>()
  var done = false
  while (!done) {
    listOf(FourDirections.DOWN, FourDirections.LEFT, FourDirections.UP, FourDirections.RIGHT).forEach { direction ->
      currentBoulders = currentBoulders.sortedBy {
        when (direction) {
          FourDirections.DOWN -> it.y
          FourDirections.UP -> -it.y
          FourDirections.LEFT -> it.x
          FourDirections.RIGHT -> -it.x
        }
      }
      currentBoulders = coordinates.moveAll(currentBoulders, direction)
    }
    count++
    if (currentBoulders to coordinates in seen) {
      done = true
    } else {
      seen.add(currentBoulders to coordinates)
      yLoad.add(currentBoulders.sumOf { maxY - it.y })
    }
  }
  val startLoop = seen.indexOf(currentBoulders to coordinates)
  val loopSize = seen.size - startLoop
  val index = startLoop + (1000000000 - startLoop) % loopSize - 1
  return yLoad[index]
}


fun MutableMap<Coordinate, Char>.moveAll(boulders: List<Coordinate>, direction: FourDirections) =
  boulders.map { coordinate ->
    var current = coordinate
    var next = canGoDirection(coordinate, direction)
    while (next != null) {
      this[current] = '.'
      this[next] = 'O'
      current = next
      next = canGoDirection(current, direction)
    }
    current
  }

fun Map<Coordinate, Char>.canGoDirection(coordinate: Coordinate, direction: FourDirections) = when {
  get(coordinate.plus(direction.direction)) == '.' -> coordinate.plus(direction.direction)
  else -> null
}
