package day15.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 15/12/2023
 */
fun day15Part1(input: BufferedReader): Any {
  return input.readLine().split(",").sumOf {
    hash(it)
  }
}

fun hash(string: String) = string.fold(0) { n, c ->
  (n + c.code) * 17 % 256
}
