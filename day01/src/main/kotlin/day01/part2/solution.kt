package day01.part2

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 01/12/2023
 */
fun day01Part2(input: BufferedReader): Any {
  return input.lineSequence().map { line ->
      val a = line.findFirst()
      val b = line.findLast()
      "$a$b"
    }.map { it.toInt() }.sum()
}

fun String.findFirst() = listOf(
  1 to indexOf("1"),
  2 to indexOf("2"),
  3 to indexOf("3"),
  4 to indexOf("4"),
  5 to indexOf("5"),
  6 to indexOf("6"),
  7 to indexOf("7"),
  8 to indexOf("8"),
  9 to indexOf("9"),
  1 to indexOf("one"),
  2 to indexOf("two"),
  3 to indexOf("three"),
  4 to indexOf("four"),
  5 to indexOf("five"),
  6 to indexOf("six"),
  7 to indexOf("seven"),
  8 to indexOf("eight"),
  9 to indexOf("nine")
).filter { it.second > -1 }.minBy { it.second }.first

fun String.findLast() = listOf(
  1 to lastIndexOf("1"),
  2 to lastIndexOf("2"),
  3 to lastIndexOf("3"),
  4 to lastIndexOf("4"),
  5 to lastIndexOf("5"),
  6 to lastIndexOf("6"),
  7 to lastIndexOf("7"),
  8 to lastIndexOf("8"),
  9 to lastIndexOf("9"),
  1 to lastIndexOf("one"),
  2 to lastIndexOf("two"),
  3 to lastIndexOf("three"),
  4 to lastIndexOf("four"),
  5 to lastIndexOf("five"),
  6 to lastIndexOf("six"),
  7 to lastIndexOf("seven"),
  8 to lastIndexOf("eight"),
  9 to lastIndexOf("nine")
).maxBy { it.second }.first


