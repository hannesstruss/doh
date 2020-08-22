package doh.web

import doh.db.DoughStatus
import kotlinx.serialization.Serializable

@Serializable
data class DoughStatusViewModel(
  val id: String,
  val recordedAt: String,
  val recordedAtEpochSeconds: Long,
  val imagePath: String,
  val growth: Double
) {
  companion object {
    fun fromDoughStatus(imagesPath: String, doughStatus: DoughStatus) = DoughStatusViewModel(
      id = doughStatus.id.toString(),
      recordedAt = doughStatus.recordedAt.toString(),
      recordedAtEpochSeconds = doughStatus.recordedAt.epochSecond,
      imagePath = "$imagesPath/${doughStatus.imageFile}",
      growth = doughStatus.growth
    )
  }
}
