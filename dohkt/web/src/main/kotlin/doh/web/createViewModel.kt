package doh.web

import doh.db.DoughStatus
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("d.LL.uuuu, HH:mm")

fun DoughStatusViewModel.Companion.fromDoughStatus(imagesPath: String, doughStatus: DoughStatus) = DoughStatusViewModel(
  id = doughStatus.id.toString(),
  recordedAt = doughStatus.recordedAt.atZone(ZoneId.of("Europe/Berlin")).format(formatter),
  recordedAtEpochSeconds = doughStatus.recordedAt.epochSecond,
  backlitImagePath = "$imagesPath/${doughStatus.backlitImageFile}"
)
