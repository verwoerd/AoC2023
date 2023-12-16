package day16.part2

import Coordinate
import FourDirections
import day16.part1.calculateLights
import day16.part1.parseMaze
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 16/12/2023
 */
fun day16Part2(input: BufferedReader): Any {
  val map = input.parseMaze()
  val yRange = map.yRange().let { (a, b) -> a..b }
  val xRange = map.xRange().let { (a, b) -> a..b }
  var maxFound = 0
  yRange.forEach { y ->
    maxFound = maxFound.coerceAtLeast(map.calculateLights(Coordinate(xRange.first, y), FourDirections.RIGHT))
    maxFound = maxFound.coerceAtLeast(map.calculateLights(Coordinate(xRange.last, y), FourDirections.LEFT))
  }
  xRange.forEach { x ->
    maxFound = maxFound.coerceAtLeast(map.calculateLights(Coordinate(x, yRange.first), FourDirections.UP))
    maxFound = maxFound.coerceAtLeast(map.calculateLights(Coordinate(x, yRange.last), FourDirections.DOWN))
  }
  return maxFound
}
