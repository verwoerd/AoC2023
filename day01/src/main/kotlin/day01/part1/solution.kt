package day01.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 01/12/2023
 */
fun day01Part1(input: BufferedReader): Any {
  return input.lineSequence().map { line ->
      val a = line.first { it.isDigit() }
      val b = line.last { it.isDigit() }
      "$a$b"
    }.map { it.toInt() }.sum()
}
