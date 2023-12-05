package day05.part2

import day05.part1.Almanac
import day05.part1.parseAlmanac
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 05/12/2023
 */
fun day05Part2(input: BufferedReader): Any {
  val almanac = input.parseAlmanac()
  val seedsRanges = almanac.seeds.windowed(2, step = 2).map { (a, b) -> a..<a + b }
  // Too Slow, but by using sequences, the jvm does not run out of memory and results in 5m runtime for input
  /*return seedRanges
    .minOfOrNull { range -> range.asSequence().map { almanac.lookupSeedDetails(it) }
      .minOf { it.location } } ?: error("something went wrong")*/
  // reverse lookup, iterate over location until you find a seed in the range. Runs in 8s
  return (0..Long.MAX_VALUE).asSequence().map { almanac.reverseLookupLocation(it) to it }
    .first { (seed) -> seedsRanges.any { seed in it } }.second

  // being smart attempt 2, sorting by location for lowest value, no real improvement
  /*val orders = almanac.humidityToLocation.map { it.second }.sortedBy { it.first }.toCollection(LinkedList())
  var last = -1L
  fun LongRange.findSeed(): Long? =asSequence().map { almanac.reverseLookupLocation(it) to it }
    .firstOrNull { (seed) -> seedsRanges.any { seed in it } }?.second


  while (orders.isNotEmpty()) {
    val current = orders.removeFirst()
    if (current.first != last + 1) {
      val result = (last + 1..<current.first).findSeed()
      if (result != null) return result
    }
    val result = current.findSeed()
    if (result != null) return result
    last = current.last
  }
  return (last+1 .. Long.MAX_VALUE).findSeed()!!*/
}

fun Almanac.reverseLookupLocation(location: Long): Long {
  val humidity = reverseLookup(humidityToLocation, location)
  val temperature = reverseLookup(temperatureToHumidity, humidity)
  val light = reverseLookup(lightToTemperature, temperature)
  val water = reverseLookup(waterToLight, light)
  val fertilizer = reverseLookup(fertilizerToWater, water)
  val soil = reverseLookup(soilToFertilizer, fertilizer)
  return reverseLookup(seedsToSoil, soil)
}

fun Almanac.reverseLookup(chapter: List<Pair<LongRange, LongRange>>, target: Long): Long {
  val (from, to) = chapter.firstOrNull { target in it.second } ?: return target
  return from.first + target - to.first
}

