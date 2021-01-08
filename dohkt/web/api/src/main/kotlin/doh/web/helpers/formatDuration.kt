package doh.web.helpers

import java.time.Instant
import kotlin.math.absoluteValue

private val M_IN_S = 60L
private val H_IN_S = M_IN_S * 60L
private val D_IN_S = H_IN_S * 24L

fun Instant.formattedDuration(now: Instant): String {

  val nowSecond = now.epochSecond
  val thenSecond = this.epochSecond

  val secondsDifference = nowSecond - thenSecond

  val (duration, unit) = when (secondsDifference.absoluteValue) {
    in (0 until M_IN_S - 1) -> secondsDifference to "second"
    in (M_IN_S until D_IN_S) -> secondsDifference / M_IN_S to "minute"
    else -> secondsDifference / D_IN_S to "day"
  }

  val durationWithUnit = "$duration $unit" + if (duration != 1L) "s" else ""

  return if (secondsDifference >= 0) {
    "$durationWithUnit ago"
  } else {
    "in $durationWithUnit"
  }
}
