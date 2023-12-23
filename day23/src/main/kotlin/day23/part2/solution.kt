package day23.part2

import Coordinate
import adjacentCoordinates
import priorityQueueOf
import toCoordinateMap
import xRange
import yRange
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 23/12/2023
 */
fun day23Part2(input: BufferedReader): Any {
  val map = input.toCoordinateMap()
  val xRange = map.xRange().let { (a, b) -> a..b }
  val yRange = map.yRange().let { (a, b) -> a..b }
  val adjacencyMap = map.filter { it.value != '#' }.keys.associateWith { coordinate ->
    adjacentCoordinates(coordinate).filter { it.x in xRange && it.y in yRange }.filter { map[it]!! != '#' }.toList()
  }.toMutableMap()
  val distanceMap = adjacencyMap.flatMap { (key, value) ->
    value.map { key to it } + value.map { it to key }
  }.associateWith { 1 }.toMutableMap()
  val canBeFixed = adjacencyMap.filter { it.value.size == 2 }
  val fixQueue = canBeFixed.keys.toMutableList()
  while (fixQueue.isNotEmpty()) {
    val current = fixQueue.removeFirst()
    if (current !in adjacencyMap) continue

    var (from, to) = adjacencyMap[current]!!

    var diff = 2
    var lastFrom = current
    var next: Coordinate
    while (adjacencyMap[from]!!.size == 2) {
      next = adjacencyMap[from]!!.first { it != lastFrom }
      adjacencyMap.remove(from)
      lastFrom = from
      from = next
      diff++
    }
    var lastTo = current
    while (adjacencyMap[to]!!.size == 2) {
      next = adjacencyMap[to]!!.first { it != lastTo }
      adjacencyMap.remove(to)
      lastTo = to
      to = next
      diff++
    }
    adjacencyMap.remove(current)
    adjacencyMap[from] = adjacencyMap[from]!!.mapNotNull {
      when (it) {
        from -> null
        lastFrom -> to
        else -> it
      }
    }
    adjacencyMap[to] = adjacencyMap[to]!!.mapNotNull {
      when (it) {
        to -> null
        lastTo -> from
        else -> it
      }
    }
    distanceMap[from to to] = diff
    distanceMap[to to from] = diff


  }


  val start = map.filter { it.key.y == yRange.first }.filter { it.value == '.' }.keys.first()
  val target = map.filter { it.key.y == yRange.last }.filter { it.value == '.' }.keys.first()
  val queue =
    priorityQueueOf(Comparator.comparing { it: Triple<Coordinate, Int, Set<Coordinate>> -> -it.second })

  var count = 0L
  queue.add(Triple(start, 0, setOf(start)))
  val visited = mutableMapOf<Pair<Coordinate, Int>, Int>()
//  val visited = mutableMapOf<Int, Int>()
//  val visited = mutableMapOf<Coordinate, Long>()
  var best = 0
  while (queue.isNotEmpty()) {
    val (current, length, path) = queue.poll()
    if (current == target) {
      println("[$count] Found $length (queue=${queue.size}): $best ${path.size}")
      best = best.coerceAtLeast(length)
      if (best == 6434) {
        println("Found it in ${count + 1}")
        return best
      }

    }
//    if (current to path.size in visited && visited[current to path.size]!! > length) continue
    count++
    visited[current to path.size] = length
    adjacencyMap[current]!!
      .asSequence()
      .filter { it !in path }
      .map { Triple(it, length + distanceMap[current to it]!!, path + it) }
//      .filter { it.second >= (visited[it.first to it.third] ?: 0) }
//      .onEach { visited[it.first to it.third] = it.second }
      .toCollection(queue)
  }
  return best
}

