package day16.part1

import Coordinate
import FourDirectionFlipped
import FourDirectionFlipped.*
import origin
import toSpecializedCoordinateMap
import xRange
import yRange
import java.io.BufferedReader
import java.util.*

/**
 * @author verwoerd
 * @since 16/12/2023
 */
fun day16Part1(input: BufferedReader): Any {
  val map = input.parseMaze()
  return map.calculateLights(origin, RIGHT)
}

fun Map<Coordinate, Mirror>.calculateLights(start: Coordinate = origin, startDirection: FourDirectionFlipped): Int {
  val yRange = yRange().let { (a, b) -> a..b }
  val xRange = xRange().let { (a, b) -> a..b }
  val lights = Array(yRange.last + 1) { IntArray(xRange.last + 1) }
  val queue = LinkedList<Pair<Coordinate, FourDirectionFlipped>>()
  queue.add(start to startDirection)
  val visited = mutableSetOf<Pair<Coordinate, FourDirectionFlipped>>()

  while (queue.isNotEmpty()) {
    val (current, direction) = queue.removeFirst()
    if (current.x !in xRange || current.y !in yRange) continue
    if (!visited.add(current to direction)) continue
    lights[current.y][current.x]++
    val mirror = get(current)!!
    val next = mirror.move(current, direction)
    queue.addAll(next)
  }
  return lights.sumOf { line -> line.count { it > 0 } }
}

enum class Mirror(
  val char: Char,
  val move: (Coordinate, FourDirectionFlipped) -> List<Pair<Coordinate, FourDirectionFlipped>>
) {
  EMPTY('.', { coordinate, direction -> listOf(direction + coordinate to direction) }),
  MIRROR_UP('/', { coordinate, direction ->
    when (direction) {
      DOWN -> listOf(LEFT).map { it + coordinate to it }
      UP -> listOf(RIGHT).map { it + coordinate to it }
      LEFT -> listOf(DOWN).map { it + coordinate to it }
      RIGHT -> listOf(UP).map { it + coordinate to it }
    }
  }),
  MIRROR_DOWN('\\', { coordinate, direction ->
    when (direction) {
      DOWN -> listOf(RIGHT).map { it + coordinate to it }
      UP -> listOf(LEFT).map { it + coordinate to it }
      LEFT -> listOf(UP).map { it + coordinate to it }
      RIGHT -> listOf(DOWN).map { it + coordinate to it }
    }
  }),
  SPLITTER_VERTICAL('|', { coordinate, direction ->
    when (direction) {
      DOWN, UP -> listOf(direction).map { it.direction + coordinate to it }
      LEFT, RIGHT -> listOf(UP, DOWN).map { it.direction + coordinate to it }
    }
  }),
  SPLITTER_HORIZONTAL('-', { coordinate, direction ->
    when (direction) {
      DOWN, UP -> listOf(LEFT, RIGHT).map { it.direction + coordinate to it }
      LEFT, RIGHT -> listOf(direction).map { it.direction + coordinate to it }
    }
  });

  companion object {
    fun fromChar(char: Char) = entries.first { it.char == char }
  }
}

fun BufferedReader.parseMaze() = toSpecializedCoordinateMap(Mirror::fromChar)
