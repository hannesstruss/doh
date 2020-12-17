package doh.grab

sealed class AnalyzerResult {
  object GlassNotPresent : AnalyzerResult()
  data class GlassPresent(
    /**
     * Growth of the dough above the rubber band. 0.0 equals no growth,
     * 1.0 means dough has doubled.
     */
    val growth: Double
  ) : AnalyzerResult()
}
