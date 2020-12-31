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
