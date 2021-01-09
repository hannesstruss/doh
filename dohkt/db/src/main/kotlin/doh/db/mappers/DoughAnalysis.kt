package doh.db.mappers

import doh.db.DoughAnalysis
import doh.shared.AnalyzerResult

fun DoughAnalysis.toAnalyzerResult(): AnalyzerResult.GlassPresent {
  return AnalyzerResult.GlassPresent(
    rubberBandY = this.rubberBandY.toInt(),
    glassBottomY = this.glassBottomY.toInt(),
    doughLevelY = this.doughLevelY.toInt()
  )
}
