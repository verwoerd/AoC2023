package day02.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 02/12/2023
 */
fun day02Part1(input: BufferedReader): Any {
  val maxRed = 12
  val maxGreen = 13
  val maxBlue = 14
  return input.lineSequence().map { parseGame(it) }
    .filter { it.second.all { (red, blue, green) -> red <= maxRed && green <= maxGreen && blue <= maxBlue } }
    .sumOf { it.first }
}

data class ColorGrouping(
  var red: Int = 0, var blue: Int = 0, var green: Int = 0
) {
  fun addColor(amount: Int, color: String): ColorGrouping {
    return when (color) {
      "red" -> copy(red = amount)
      "blue" -> copy(blue = amount)
      "green" -> copy(green = amount)
      else -> error("unkown color $color")
    }
  }
}


fun parseGame(line: String): Pair<Int, List<ColorGrouping>> {
  val (start, end) = line.split(": ")
  return start.dropWhile { it != ' ' }.drop(1).toInt() to end.split("; ").map { round ->
    round.split(", ").map { part -> part.split(' ').let { (a, b) -> a.toInt() to b } }
      .fold(ColorGrouping()) { acc, (a, c) -> acc.addColor(a, c) }
  }
}

