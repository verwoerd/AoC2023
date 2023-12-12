package day12.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 12/12/2023
 */
fun day12Part1(input: BufferedReader): Any {
  val parsed = input.parseInput()
  return parsed.sumOf { (line, groups) -> searchOptions(line, groups) }
}

data class CacheEntry(
  val sequence: String, val groups: List<Long>, val lastChar: Char, val currentGroupSize: Int
)

val cache = mutableMapOf(CacheEntry("", emptyList(), '.', 0) to 1L, CacheEntry("", emptyList(), '#', 0) to 1L)

fun searchOptions(line: String, groups: List<Long>, groupSize: Int = 0, lastChar: Char = '$'): Long {
  cache[CacheEntry(line, groups, lastChar, groupSize)]?.let { return it }
  if (line.isEmpty() && (groups.isNotEmpty() || groupSize > 0)) return 0
  if (line.isEmpty()) {
    error("Shouldn't get here")
  }
  val current = line.last()
  val currentGroup = groups.lastOrNull() ?: 0
  val result = when (current) {
    '.' -> when {
      groupSize > 0 -> 0
      else -> searchOptions(line.dropLast(1), groups, groupSize, current)
    }

    '#' -> when {
      lastChar == '#' && groupSize == 0 -> 0
      currentGroup - groupSize <= 0 -> 0
      currentGroup - groupSize == 1L -> searchOptions(line.dropLast(1), groups.dropLast(1), 0, current)
      else -> searchOptions(line.dropLast(1), groups, groupSize + 1, current)
    }

    else -> when {
      groupSize > 0 && currentGroup - groupSize == 1L -> searchOptions(
        line.dropLast(1), groups.dropLast(1), 0, '#'
      ) // must be last # of group
      groupSize > 0 -> searchOptions(line.dropLast(1), groups, groupSize + 1, '#') // must be #
      currentGroup == 0L -> searchOptions(line.dropLast(1), groups, groupSize, '.')   // must be .
      lastChar == '#' -> searchOptions(line.dropLast(1), groups, groupSize, '.') // must be .
      currentGroup == 1L -> searchOptions(
        line.dropLast(1), groups.dropLast(1), 0, '#'
      ) + searchOptions(line.dropLast(1), groups, 0, '.') // special case, drop a group for #

      else -> searchOptions(line.dropLast(1), groups, 1, '#') + searchOptions(line.dropLast(1), groups, 0, '.')
    }

  }
  cache[CacheEntry(line, groups, lastChar, groupSize)] = result
  return result

}


fun BufferedReader.parseInput() = lineSequence().map { line ->
  line.split(" ", limit = 2).let { (a, b) -> a to b.split(",").map { it.toLong() } }
}.toList()


/*
7846 -- not right

?#?#?#?#?#?#?#? 1,3,1,6
.#.###.#.######
.#.###.#.###### // iterantion 1
?#?#?#?#######. // debugging cache for the last bug
 */
