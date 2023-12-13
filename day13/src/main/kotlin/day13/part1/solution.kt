package day13.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 13/12/2023
 */
fun day13Part1(input: BufferedReader): Any {
  val patterns = input.readPatterns()
  return patterns.sumOf { list ->
    list.findHorizontalMirroring()?.let { (it + 1) * 100 } ?: list.findVerticalMirroring()?.let { it + 1 }
    ?: error("NO mirror found")
  }
}

fun List<String>.findHorizontalMirroring() = indices
  .filter { matchNextNeighboursHorizontal(it) }.firstOrNull { isHorizontalMirror(it) }

fun List<String>.matchNextNeighboursHorizontal(index: Int) =
  index + 1 in indices && get(index) == get(index + 1)

fun List<String>.isHorizontalMirror(index: Int) =
  (1..<size).takeWhile { index + it + 1 in indices && index - it in indices }
    .all { delta -> get(index + delta + 1) == get(index - delta) }

fun List<String>.findVerticalMirroring() = first().indices
  .filter { matchNextNeighbours(it) }.firstOrNull { isVerticalMirror(it) }

fun List<String>.matchNextNeighbours(index: Int): Boolean {
  return index + 1 in first().indices && all { it[index] == it[index + 1] }
}

fun List<String>.isVerticalMirror(index: Int): Boolean =
  (1..<size).takeWhile { index + it + 1 in first().indices && index - it in first().indices }
    .all { delta -> all { it[index + delta + 1] == it[index - delta] } }


fun BufferedReader.readPatterns() = lineSequence()
  .fold(mutableListOf<List<String>>() to emptyList<String>()) { (list, current), s ->
    when {
      s.isBlank() -> list.also { it.add(current) } to emptyList()
      else -> list to current + s
    }
  }.let { (l, n) -> l.also { it.add(n) } }
