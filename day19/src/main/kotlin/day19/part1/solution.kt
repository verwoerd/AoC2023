package day19.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 19/12/2023
 */
fun day19Part1(input: BufferedReader): Any {
  val (rules, assignments) = input.readInput()
  return assignments.filter { assignment ->
    var current = "in"
    while (current != "A" && current != "R") {
      val rule = rules[current]!!
      current = (rule.decissions.firstOrNull { it.test(assignment) }?.successLabel) ?: rule.elseRule
    }
    current == "A"
  }.sumOf { it.total() }
}

data class Condition(
  val field: Char,
  val lessThen: Boolean,
  val value: Long,
  val successLabel: String
) {
  fun test(assignment: Assignment): Boolean {
    val target = when (field) {
      'x' -> assignment.x
      'm' -> assignment.m
      'a' -> assignment.a
      's' -> assignment.s
      else -> error(this)
    }
    return when (lessThen) {
      true -> target < value
      else -> target > value
    }
  }
}

data class SortRule(
  val name: String,
  val decissions: List<Condition>,
  val elseRule: String
)

data class Assignment(
  val x: Long,
  val m: Long,
  val a: Long,
  val s: Long
) {
  fun total() = x + m + a + s
}

fun BufferedReader.readInput(): Pair<Map<String, SortRule>, List<Assignment>> {
  val lines = readLines()
  val rules = lines.takeWhile { it.isNotBlank() }.associate { line ->
    val (name, rules) = line.split('{', limit = 2)
    val splitRules = rules.split(',')
    val elseCondition = splitRules.last().dropLast(1)
    val conditions = splitRules.dropLast(1).map {
      val (test, label) = it.split(':', limit = 2)
      val conditionName = test.first()
      val lessThen = test[1] == '<'
      val value = test.drop(2).toLong()
      Condition(conditionName, lessThen, value, label)
    }
    name to SortRule(name, conditions, elseCondition)
  }

  val regex = Regex("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}")
  val assignments = lines.dropWhile { it.isNotBlank() }.drop(1).map { line ->
    regex.matchEntire(line)!!.destructured.let { (x, m, a, s) ->
      Assignment(
        x.toLong(),
        m.toLong(),
        a.toLong(),
        s.toLong()
      )
    }
  }
  return rules to assignments
}
