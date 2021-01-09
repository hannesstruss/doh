package doh.grab

import doh.shared.AnalyzerResult
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzerResponse(
  val glass_present: Boolean,
  val glass_data: GlassData?
) {
  companion object {
    fun toResult(response: AnalyzerResponse): AnalyzerResult {
      return if (response.glass_present && response.glass_data != null) {
        AnalyzerResult.GlassPresent(
          rubberBandY = response.glass_data.rubber_band_y,
          glassBottomY = response.glass_data.glass_bottom_y,
          doughLevelY = response.glass_data.dough_level_y
        )
      } else {
        AnalyzerResult.GlassNotPresent
      }
    }
  }
}

@Serializable
data class GlassData(
  val rubber_band_y: Int,
  val glass_bottom_y: Int,
  val dough_level_y: Int
)
