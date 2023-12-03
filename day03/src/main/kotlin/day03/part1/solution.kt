package day03.part1

import Coordinate
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 03/12/2023
 */

var maxX = 0
var maxY = 0
fun day03Part1(input: BufferedReader): Any {
  val (grid, symbols) = input.readGrid()
  return symbols.flatMap { (coordinate) -> grid.getAdjacentNumbers(coordinate) }.sum()
}

fun BufferedReader.readGrid(): Pair<List<String>, List<Pair<Coordinate, Char>>> {
  val grid = lines().toList()
  maxX = grid[0].length - 1
  maxY = grid.size - 1
  val symbols = (0..maxY).flatMap { y ->
    (0..maxX).mapNotNull { x ->
      val current = grid[y][x]
      if (current != '.' && !current.isDigit()) {
        Coordinate(x, y) to current
      } else null
    }
  }
  return grid to symbols
}

fun List<String>.getAdjacentNumbers(coordinate: Coordinate): MutableList<Long> {
  val (x, y) = coordinate
  val current = mutableListOf<Long>()
  if (y > 0) {
    if (x > 0 && this[y - 1][x - 1].isDigit()) {
      current += this.extractDigit(y - 1, x - 1)
    }
    if (this[y - 1][x].isDigit() && (x == 0 || !this[y - 1][x - 1].isDigit())) {
      current += this.extractDigit(y - 1, x)
    }
    if (x < maxX && !this[y - 1][x].isDigit() && this[y - 1][x + 1].isDigit()) {
      current += this.extractDigit(y - 1, x + 1)
    }
  }
  if (x > 0 && this[y][x - 1].isDigit()) {
    current += this.extractDigit(y, x - 1)
  }
  if (x < maxX && this[y][x + 1].isDigit()) {
    current += this.extractDigit(y, x + 1)
  }
  if (y < maxY) {
    if (x > 0 && this[y + 1][x - 1].isDigit()) {
      current += this.extractDigit(y + 1, x - 1)
    }
    if (this[y + 1][x].isDigit() && (x == 0 || !this[y + 1][x - 1].isDigit())) {
      current += this.extractDigit(y + 1, x)
    }
    if (x < maxX && !this[y + 1][x].isDigit() && this[y + 1][x + 1].isDigit()) {
      current += this.extractDigit(y + 1, x + 1)
    }
  }
  return current
}


fun List<String>.extractDigit(y: Int, x: Int): Long {
  var start = x - 1
  while (start >= 0 && get(y)[start].isDigit()) start--
  var end = x + 1
  while (end <= maxX && get(y)[end].isDigit()) end++
  return get(y).substring(start + 1, end).toLong()
}
