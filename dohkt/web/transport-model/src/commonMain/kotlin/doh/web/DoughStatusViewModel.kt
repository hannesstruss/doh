package doh.web

import kotlinx.serialization.Serializable

@Serializable
data class DoughStatusViewModel(
  val id: String,
  val recordedAt: String,
  val recordedAtEpochSeconds: Long,
  val backlitImagePath: String,
  val ambientImagePath: String?
) {
  companion object
}
