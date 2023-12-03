package day03.part2

import day03.part1.getAdjacentNumbers
import day03.part1.readGrid
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 03/12/2023
 */
fun day03Part2(input: BufferedReader): Any {
  val (grid, symbols) = input.readGrid()
  return symbols.map { (coordinate, symbol) -> grid.getAdjacentNumbers(coordinate) to symbol }
    .sumOf { (current, symbol) ->
      when {
        current.size == 2 && symbol == '*' -> current[0] * current[1]
        else -> 0
      }
    }

}
