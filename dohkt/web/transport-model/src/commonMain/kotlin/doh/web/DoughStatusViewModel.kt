package doh.web

import kotlinx.serialization.Serializable
import doh.shared.AnalyzerResult

@Serializable
data class DoughStatusViewModel(
  val id: String,
  val recordedAt: String,
  val recordedAtEpochSeconds: Long,
  val backlitImagePath: String,
  val ambientImagePath: String?,
  val doughData: AnalyzerResult.GlassPresent?
) {
  companion object
}
