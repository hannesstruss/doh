package doh.shared

sealed class AnalyzerResult {
  object GlassNotPresent : AnalyzerResult()
  data class GlassPresent(
    val rubberBandY: Int,
    val glassBottomY: Int,
    val doughLevelY: Int
  ) : AnalyzerResult()
}
