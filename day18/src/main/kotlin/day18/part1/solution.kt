package day18.part1

import Coordinate
import FourDirectionFlipped
import origin
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 18/12/2023
 */
fun day18Part1(input: BufferedReader): Any {
  val digInstruction = input.readDigInstructions()
  val map = mutableMapOf(origin to "#000000")
  map.executeInstructions(origin, digInstruction)
  return map.calculateTrench()
}

fun MutableMap<Coordinate, String>.calculateTrench(): Int {
  val xRange = xRange().let { (a, b) -> a..b }
  val yRange = yRange().let { (a, b) -> a..b }
  val connectNorth = keys.filter { FourDirectionFlipped.UP + it in this }.toSet()
  var count = 0
  yRange.forEach { y ->
    var border = 0
    xRange.forEach { x ->
      val coordinate = Coordinate(x, y)
      border += when {
        coordinate in connectNorth -> 1
        else -> 0
      }
      when {
        containsKey(coordinate) -> count++
        border % 2 == 1 -> count++
      }
    }
  }
  return count
}

fun MutableMap<Coordinate, String>.executeInstructions(start: Coordinate, instructions: Sequence<DigInstruction>) {
  var current = start
  instructions.forEach { instruction ->
    repeat(instruction.amount) {
      current = instruction.direction + current
      set(current, instruction.colorCode)
    }
  }
}


data class DigInstruction(
  val direction: FourDirectionFlipped, val amount: Int, val colorCode: String
) {
  fun transformInstruction(): DigInstruction {
    val steps = colorCode.drop(1).take(5).toInt(16)
    val direction = when (colorCode.last()) {
      '0' -> FourDirectionFlipped.RIGHT
      '1' -> FourDirectionFlipped.DOWN
      '2' -> FourDirectionFlipped.LEFT
      '3' -> FourDirectionFlipped.UP
      else -> error("invalid $colorCode")
    }
    return copy(direction = direction, amount = steps)
  }
}

fun BufferedReader.readDigInstructions() = lineSequence().map { line ->
  line.split(" ", limit = 3).let { (a, b, c) ->
    DigInstruction(
      when (a) {
        "R" -> FourDirectionFlipped.RIGHT
        "L" -> FourDirectionFlipped.LEFT
        "U" -> FourDirectionFlipped.UP
        "D" -> FourDirectionFlipped.DOWN
        else -> error("Invalid input : $a $b $c")
      }, b.toInt(), c.drop(1).dropLast(1)
    )
  }
}
