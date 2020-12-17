package doh.grab

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzerResponse(
  val glass_present: Boolean,
  val glass_data: GlassData?
)

@Serializable
data class GlassData(
  val rubber_band_y: Int,
  val glass_bottom_y: Int,
  val dough_level_y: Int
)
