package day18.part2

import Coordinate
import FourDirectionFlipped
import day18.part1.readDigInstructions
import origin
import pointsOnBoundary
import polygonArea
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 18/12/2023
 */
fun day18Part2(input: BufferedReader): Any {
  val polygon = input.readDigInstructions().map { it.transformInstruction() }
    .runningFold(origin) { current, instruction ->
      when (instruction.direction) {
        FourDirectionFlipped.RIGHT -> Coordinate(current.x + instruction.amount, current.y)
        FourDirectionFlipped.LEFT -> Coordinate(current.x - instruction.amount, current.y)
        FourDirectionFlipped.DOWN -> Coordinate(current.x, current.y + instruction.amount)
        FourDirectionFlipped.UP -> Coordinate(current.x, current.y - instruction.amount)
      }
    }.toList()
  // Pick's theory
  // A = I + B/2 -1 -> I = A - B/2 + 1
  val area = polygon.polygonArea()
  val boundary = polygon.pointsOnBoundary()
  // Points inside the polygon + points on the boundary
  return (area - boundary / 2 + 1).toLong() + boundary
}


