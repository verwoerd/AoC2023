package day24.part1

import TripleCoordinate
import java.io.BufferedReader

/**
 * @author verwoerd
 * @since 24/12/2023
 */
fun day24Part1(input: BufferedReader, test: LongRange = 200000000000000..400000000000000): Any {
  val hailstones = input.readInput()
  return hailstones.mapIndexed { index, hailstone ->
    hailstones.drop(index + 1).count { hailstone.collide2D(it, test) }
  }.sum()
}


data class Hailstone(
  val start: TripleCoordinate,
  val velocity: TripleCoordinate
) {
  private val slope = velocity.y.toDouble() / velocity.x
  private val intersectionYaxis = start.y.toDouble() - slope * start.x
  private fun findT(xValue: Double) = (xValue - start.x) / velocity.x

  private fun f(t: Long) = TripleCoordinate(start.x + velocity.x*t, start.y + velocity.y*t,start.z + velocity.z*t)

  fun collide2D(other: Hailstone, range: LongRange): Boolean {
    if (this.slope == other.slope) return false // parallel
    val xIntersect = (other.intersectionYaxis - intersectionYaxis) / (slope - other.slope)
    val yIntersect = slope * xIntersect + intersectionYaxis
    val myT = findT(xIntersect)
    val otherT = other.findT(xIntersect)
    if (myT < 0 || otherT < 0) return false // intersection in the past
    return xIntersect.toLong() in range && yIntersect.toLong() in range
  }

  fun collide3D(other: Hailstone, range: LongRange) =
    range.any { f(it)== other.f(it) }

  fun collide2DByCalculatingT(other: Hailstone, range: LongRange): Boolean {
    println("considering $this and $other")
    val x = start.x.toDouble()
    val y = start.y.toDouble()
    val f = velocity.x.toDouble()
    val g = velocity.y.toDouble()
    val v = other.start.x.toDouble()
    val w = other.start.y.toDouble()
    val k = other.velocity.x.toDouble()
    val l = other.velocity.y.toDouble()

    val q = (y * g * f + g * f * g * (v / f) - g * f * g * (x / f) - v * g * f) / (g * f * l - k)
    val t = (v + k * q - x) / f
    val xCross1 = x + f * t
    val xCross2 = v + k * q
    val yCross1 = y + g * t
    val yCross2 = w + l * q
    assert(xCross1 == xCross2) { "Error calcualting x corring point $xCross1 $xCross2 delta = ${xCross1 - xCross2}" }
    assert(yCross1 == yCross2) { "Error calcualting y crossing point $yCross1 $yCross2 delta = ${yCross1 - yCross2}" }
    return xCross1.toLong() in range && yCross1.toLong() in range

  }
}

fun BufferedReader.readInput() =
  lineSequence().map { line ->
    line.split(Regex("[@,]"), limit = 6).map { it.trim().toLong() }.let { value ->
      Hailstone(TripleCoordinate(value[0], value[1], value[2]), TripleCoordinate(value[3], value[4], value[5]))
    }
  }.toList()
