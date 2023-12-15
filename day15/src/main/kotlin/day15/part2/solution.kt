package day15.part2

import day15.part1.hash
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 15/12/2023
 */
fun day15Part2(input: BufferedReader): Any {
  val operations = input.readLine().split(",")
  val boxes = Array<MutableList<Entry>>(257) { mutableListOf() }
  operations.forEach { operation ->
    if (operation.last() != '-') {
      val (label, value) = operation.split("=", limit = 2)
      val box = hash(label)
      with(boxes[box]) {
        val labelIndex = indexOfFirst { it.label == label }
        if (labelIndex > -1) {
          set(labelIndex, Entry(label, value.toLong()))
        } else {
          add(Entry(label, value.toLong()))
        }
      }
    } else {
      val label = operation.dropLast(1)
      val box = hash(label)
      boxes[box].removeIf { it.label == label }
    }
  }
  return boxes.withIndex().sumOf { (box, contents) ->
    contents.withIndex().sumOf { (index, entry) ->
      ((box + 1) * (index + 1) * entry.value)
    }
  }
}

data class Entry(
  val label: String,
  val value: Long
)
