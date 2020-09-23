package doh.web

import doh.db.DoughStatus
import kotlinx.serialization.Serializable
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class DoughStatusViewModel(
  val id: String,
  val recordedAt: String,
  val recordedAtEpochSeconds: Long,
  val backlitImagePath: String
) {
  companion object {
    private val formatter = DateTimeFormatter.ofPattern("d.LL.uuuu, HH:mm")

    fun fromDoughStatus(imagesPath: String, doughStatus: DoughStatus) = DoughStatusViewModel(
      id = doughStatus.id.toString(),
      recordedAt = doughStatus.recordedAt.atZone(ZoneId.of("Europe/Berlin")).format(formatter),
      recordedAtEpochSeconds = doughStatus.recordedAt.epochSecond,
      backlitImagePath = "$imagesPath/${doughStatus.backlitImageFile}"
    )
  }
}
