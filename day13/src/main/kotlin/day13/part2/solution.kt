package day13.part2

import day13.part1.*
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 13/12/2023
 */
fun day13Part2(input: BufferedReader): Any {
  val patterns = input.readPatterns()
  return patterns.sumOf { list ->
    list.findHorizontalSmudgeMirroring()?.let { (it + 1) * 100 } ?: list.findVerticalSmudgeMirroring()?.let { it + 1 }
    ?: list.findHorizontalSmudgeMirrorLine()?.let { (it + 1) * 100 } ?: list.findVerticalSmudgeMirrorLine()
      ?.let { it + 1 } ?: error("NO mirror found:\n${list.joinToString("\n") { it }}")
  }
}

fun List<String>.findHorizontalSmudgeMirrorLine() = indices
  .filter { almostMatchNeighboursHorizontal(it) }.firstOrNull { isHorizontalMirror(it) }

fun List<String>.almostMatchNeighboursHorizontal(index: Int) =
  index + 1 in indices && get(index).withIndex().count { (i, value) -> get(index + 1)[i] != value } == 1

fun List<String>.findVerticalSmudgeMirrorLine() = first().indices
  .filter { almostMatchNeighboursVertical(it) }.firstOrNull { isVerticalMirror(it) }

fun List<String>.almostMatchNeighboursVertical(index: Int) =
  index + 1 in first().indices && count { it[index] != it[index + 1] } == 1


fun List<String>.findHorizontalSmudgeMirroring() = indices
  .filter { matchNextNeighboursHorizontal(it) }.firstOrNull { isHorizontalSmudgeMirror(it) }

fun List<String>.isHorizontalSmudgeMirror(index: Int): Boolean {
  val deltas = (1..<size).takeWhile { index + it + 1 in indices && index - it in indices }
  val mismatches = deltas.filter { delta -> get(index + delta + 1) != get(index - delta) }
  if (mismatches.size != 1) return false
  val (targetDelta) = mismatches
  return first().indices.count {
    get(index + 1 + targetDelta)[it] != get(index - targetDelta)[it]
  } == 1
}

fun List<String>.findVerticalSmudgeMirroring() = first().indices
  .filter { matchNextNeighbours(it) }.firstOrNull { isVerticalSmudgeMirror(it) }

fun List<String>.isVerticalSmudgeMirror(index: Int): Boolean {
  val deltas = (1..<size).takeWhile { index + it + 1 in first().indices && index - it in first().indices }
  val mismatches = deltas.filter { delta -> any { it[index + delta + 1] != it[index - delta] } }
  if (mismatches.size != 1) return false
  val (targetDelta) = mismatches
  return indices.count {
    get(it)[targetDelta + 1 + index] != get(it)[index - targetDelta]
  } == 1
}
