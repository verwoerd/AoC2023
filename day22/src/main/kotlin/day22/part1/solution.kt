package day22.part1

import TripleCoordinate
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 22/12/2023
 */
fun day22Part1(input: BufferedReader): Any {
  val brickSnapshot = input.readInput()
  val (supports, supportedBy) = brickSnapshot.settleBricks()
  return brickSnapshot.count { brick ->
    supports[brick]!!.all { supportedBy[it]!!.size > 1 }
  }
}


fun List<Pair<TripleCoordinate, TripleCoordinate>>.settleBricks(): Pair<MutableMap<Pair<TripleCoordinate, TripleCoordinate>, MutableList<Pair<TripleCoordinate, TripleCoordinate>>>, MutableMap<Pair<TripleCoordinate, TripleCoordinate>, MutableList<Pair<TripleCoordinate, TripleCoordinate>>>> {
  val supports = associateWith { mutableListOf<Pair<TripleCoordinate, TripleCoordinate>>() }.toMutableMap()
  val supportedBy = associateWith { mutableListOf<Pair<TripleCoordinate, TripleCoordinate>>() }.toMutableMap()
  val settled = mutableMapOf<TripleCoordinate, Pair<TripleCoordinate, TripleCoordinate>>()
  forEach { current ->
    val (start, end) = current
    val yRange = start.y..end.y
    val xRange = start.x..end.x
    var currentZ = start.z
    val zLevels = end.z - start.z
    val supportTiles = mutableListOf<TripleCoordinate>()
    while (currentZ > 1) {
      xRange.flatMap { x ->
        yRange.mapNotNull { y ->
          when (val c = TripleCoordinate(x, y, currentZ - 1)) {
            in settled -> c
            else -> null
          }
        }
      }.toCollection(supportTiles)
      if (supportTiles.isNotEmpty()) break
      currentZ--
    }

    xRange.flatMap { x ->
      yRange.flatMap { y ->
        (currentZ..currentZ + zLevels).map { z ->
          TripleCoordinate(x, y, z) to current
        }
      }
    }.toMap(settled)
    supportTiles.map { settled[it]!! }.distinct().map { target ->
      supportedBy.getOrPut(current) { mutableListOf() }.add(target)
      supports.getOrPut(target) { mutableListOf() }.add(current)
    }
  }
  return supports to supportedBy
}

fun BufferedReader.readInput() =
  lineSequence().map { line ->
    line.split("~", limit = 2)
      .map { it.split(",", limit = 3).let { (x, y, z) -> TripleCoordinate(x.toInt(), y.toInt(), z.toInt()) } }
      .let { (start, end) -> start to end }
  }.sortedWith { o1, o2 ->
    when (val zDiff = o1.first.z.compareTo(o2.first.z)) {
      0 -> when (val yDiff = o1.first.y.compareTo(o2.first.y)) {
        0 -> o1.first.x.compareTo(o2.first.x)
        else -> yDiff
      }

      else -> zDiff
    }
  }.toList()
