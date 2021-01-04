package doh.shared

import kotlinx.serialization.Serializable

sealed class AnalyzerResult {
  object GlassNotPresent : AnalyzerResult()

  @Serializable
  data class GlassPresent(
    val rubberBandY: Int,
    val glassBottomY: Int,
    val doughLevelY: Int
  ) : AnalyzerResult()
}

val AnalyzerResult.growth: Double?
  get() = when (this) {
    is AnalyzerResult.GlassPresent -> {
      val rubberBandHeight = (glassBottomY - rubberBandY).toDouble()
      val extraGrowth = (rubberBandY - doughLevelY).toDouble()
      if (extraGrowth < 0) {
        1.0
      } else {
        1.0 + extraGrowth / rubberBandHeight
      }
    }
    else -> null
  }

