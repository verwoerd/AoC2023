package day12.part2

import day12.part1.searchOptions
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 12/12/2023
 */
fun day12Part2(input: BufferedReader): Any {
  val parsed = input.parseInputPart2()
  return parsed.sumOf { (line, groups) -> searchOptions(line, groups) }
}


fun BufferedReader.parseInputPart2() = lineSequence().map { line ->
  line.split(" ", limit = 2).let { (a, b) ->
    "$a?".repeat(5).dropLast(1) to (b.split(",").map { it.toLong() }.let { listOf(it, it, it, it, it).flatten() })
  }
}.toList()
// wrong - 160565442273
/*
???.###???.###???.###???.###???.### // attempt 1  -> add question marks
???.###????.###????.###????.###????.### // expected output
???.###????.###????.###????.###????.###? // attempt 2 -> drop last question mark

 */
