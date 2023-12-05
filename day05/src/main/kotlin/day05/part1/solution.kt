package day05.part1

import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 05/12/2023
 */
fun day05Part1(input: BufferedReader): Any {
  val almanac = input.parseAlmanac()
  val details = almanac.getSeedDetails()
//  println(details)
  return details.minOf { it.location }
}

data class Almanac(
  val seeds: List<Long>,
  val seedsToSoil: List<Pair<LongRange, LongRange>>,
  val soilToFertilizer: List<Pair<LongRange, LongRange>>,
  val fertilizerToWater: List<Pair<LongRange, LongRange>>,
  val waterToLight: List<Pair<LongRange, LongRange>>,
  val lightToTemperature: List<Pair<LongRange, LongRange>>,
  val temperatureToHumidity: List<Pair<LongRange, LongRange>>,
  val humidityToLocation: List<Pair<LongRange, LongRange>>,
) {
  fun getSeedDetails() = seeds.map { lookupSeedDetails(it) }

  fun lookupSeedDetails(seed: Long): SeedDetails {
    val soil = lookup(seedsToSoil, seed)
    val fertilizer = lookup(soilToFertilizer, soil)
    val water = lookup(fertilizerToWater, fertilizer)
    val light = lookup(waterToLight, water)
    val temperature = lookup(lightToTemperature, light)
    val humidity = lookup(temperatureToHumidity, temperature)
    val location = lookup(humidityToLocation, humidity)
    return SeedDetails(seed, soil, fertilizer, water, light, temperature, humidity, location)
  }

  private fun lookup(chapter: List<Pair<LongRange, LongRange>>, target: Long): Long {
    val (from, to) = chapter.firstOrNull { target in it.first } ?: return target
    return to.first + target - from.first
  }

}

data class SeedDetails(
  val seed: Long,
  val soil: Long,
  val fertilizer: Long,
  val water: Long,
  val light: Long,
  val temperature: Long,
  val humidity: Long,
  val location: Long
)

fun BufferedReader.parseAlmanac(): Almanac {
  var input = lineSequence().toList()
  val seeds = input.first().drop(7).split(" ").map { it.trim().toLong() }
  input = input.drop(3)
  val seedsToSoil = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  input = input.drop(seedsToSoil.size + 2)
  val soilToFertilizer = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  input = input.drop(soilToFertilizer.size + 2)
  val fertilizerToWater = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  input = input.drop(fertilizerToWater.size + 2)
  val waterToLight = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  input = input.drop(waterToLight.size + 2)
  val lightToTemperature = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  input = input.drop(lightToTemperature.size + 2)
  val temperatureToHumidity = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  input = input.drop(temperatureToHumidity.size + 2)
  val humidityToLocation = input.takeWhile { it.isNotBlank() }.toList().parseRanges()
  return Almanac(
    seeds,
    seedsToSoil,
    soilToFertilizer,
    fertilizerToWater,
    waterToLight,
    lightToTemperature,
    temperatureToHumidity,
    humidityToLocation
  )
}

fun List<String>.parseRanges() =
  map { line -> line.split(" ", limit = 3).map { it.toLong() }.let { (a, b, c) -> b..<b + c to a..<a + c } }


