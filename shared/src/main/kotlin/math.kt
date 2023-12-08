import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun lcm(number1: Long, number2: Long): Long {
  if (number1 == 0L || number2 == 0L) return 0
  val absHigherNumber = absoluteMax(number1, number2)
  val absLowerNumber = absoluteMin(number1, number2)
  var lcm = absHigherNumber
  while (lcm % absLowerNumber != 0L) lcm += absHigherNumber
  return lcm
}

fun absoluteMax(num1: Long, num2: Long) = max(abs(num1), abs(num2))

fun absoluteMin(num1: Long, num2: Long) = min(abs(num1), abs(num2))

fun List<Long>.lcmForList(): Long = fold(1L) { lcmOfAllNumbers, number ->
  val result = lcm(lcmOfAllNumbers, number)
  if (result == 0L) return 0
  result
}
