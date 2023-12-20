package day20.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 20/12/2023
 */


fun day20Part1(input: BufferedReader, target: Int = 1000): Any {
  val (modules, assignment, conjunctionInputs) = input.parseInput()


  var count = 0
  var currentAssignment = assignment.toMutableMap()
  var currentConjugation: Map<String, Map<String, Boolean>> = conjunctionInputs
  var high = 0L
  var low = 0L
  do {
    val (a, c) = modules.executeButtonPress(currentAssignment, currentConjugation) { pulse, _ ->
      if (pulse == Pulse.HIGH) high++ else low++
    }

    count++
    currentConjugation = c
    currentAssignment = a
  } while (count < target)
  return high * low
}

fun Map<String, Module>.executeButtonPress(
  currentState: Map<String, Boolean>, conjunctionInputs: Map<String, Map<String, Boolean>>,
  body: (Pulse, String) -> Unit
): Pair<MutableMap<String, Boolean>, Map<String, MutableMap<String, Boolean>>> {
  var index = 0
  val nextState = currentState.toMutableMap()
  val nextConjunctionInputs = conjunctionInputs.mapValues { it.value.toMutableMap() }
  val queue = mutableListOf(Triple("button", Pulse.LOW, "broadcaster"))
  while (index < queue.size) {
    val (from, pulse, target) = queue[index]
    val isHigh = pulse == Pulse.HIGH
    body(pulse, target)
    val current = get(target)
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
  return nextState to nextConjunctionInputs
}

enum class Pulse {
  LOW, HIGH, NONE;
}

enum class ModuleType(
  val symbol: String, val processSignal: (high: Boolean, currentSignal: Boolean, inputs: Map<String, Boolean>) -> Pulse
) {
  FLIP_FLOP("%", { high, currentSignal, _ ->
    when (high) {
      true -> Pulse.NONE
      else -> if (currentSignal) Pulse.LOW else Pulse.HIGH
    }
  }),
  CONJUNCTION("&", { _, _, inputs ->
    when (inputs.values.all { it }) {
      true -> Pulse.LOW
      else -> Pulse.HIGH
    }
  }),
  NONE("", { _, _, _ -> Pulse.LOW });


  companion object {
    fun parseModule(name: String): Pair<ModuleType, String> {
      val type = entries.first { name.startsWith(it.symbol) }
      return type to name.drop(type.symbol.length)
    }
  }
}

data class Module(
  val name: String, val type: ModuleType, val connections: List<String>
)

fun BufferedReader.parseInput(): Triple<Map<String, Module>, Map<String, Boolean>, Map<String, MutableMap<String, Boolean>>> {
  val modules = lineSequence().associate { line ->
    val (name, connections) = line.split(" -> ", limit = 2)
    val (type, realName) = ModuleType.parseModule(name)
    realName to Module(realName, type, connections.split(", "))
  }
  val assignment = emptyMap<String, Boolean>()
  val conjunctionInputs =
    modules.filter { it.value.type == ModuleType.CONJUNCTION }.map { it.key }.associateWith { name ->
      modules.filter { it.value.connections.contains(name) }
        .map { it.key to (it.value.type == ModuleType.CONJUNCTION) }.toMap().toMutableMap()
    }
  return Triple(modules, assignment, conjunctionInputs)
}

