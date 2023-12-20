package day20.part2

import day20.part1.Pulse
import day20.part1.parseInput
import lcmForList
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 20/12/2023
 */
fun day20Part2(input: BufferedReader): Any {
  val (modules, assignment, conjunctionInputs) = input.parseInput()
  val parent = modules.filter { it.value.connections.contains("rx") }.values.first()
  val grandParents = modules.filter { it.value.connections.contains(parent.name) }.keys.toMutableSet()
  val cycles = mutableListOf<Long>()
  var count = 1L
  val countMap = grandParents.associateWith { 0L }.toMutableMap()
  val cycle = mutableMapOf<String, Long>()
  var currentAssignment = assignment
  var currentConjugation: Map<String, Map<String, Boolean>> = conjunctionInputs
  while (grandParents.isNotEmpty()) {
    var index = 0
    val nextState = currentAssignment.toMutableMap()
    val nextConjunctionInputs = currentConjugation.mapValues { it.value.toMutableMap() }
    val queue = mutableListOf(Triple("button", Pulse.LOW, "broadcaster"))
    while (index < queue.size) {
      val (from, pulse, target) = queue[index]
      val isHigh = pulse == Pulse.HIGH
      val current = modules[target]
      if (target in grandParents && !isHigh) {
        countMap[target] = countMap[target]!! + 1
        if (countMap[target] == 2L) {
          grandParents.remove(target)
          cycles.add(count - cycle[target]!!)
        }
        cycle[target] = count
      }
      if (current == null) {
        nextState[target] = isHigh
        index++
        continue
      }
      nextConjunctionInputs[target]?.set(from, isHigh)
      val inputs = nextConjunctionInputs[target] ?: emptyMap()

      val output = current.type.processSignal(isHigh, nextState[target] ?: false, inputs)
      if (output != Pulse.NONE) {
        nextState[target] = output == Pulse.HIGH
        current.connections.map { Triple(target, output, it) }.toCollection(queue)
      }
      index++
    }
    count++
    currentConjugation = nextConjunctionInputs
    currentAssignment = nextState
  }
  return (cycles.lcmForList())
}
