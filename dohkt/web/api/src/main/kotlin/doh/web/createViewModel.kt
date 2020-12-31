package doh.web

import doh.db.DoughStatus
import doh.shared.AnalyzerResult
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("d.LL.uuuu, HH:mm")

fun DoughStatusViewModel.Companion.fromDoughStatus(imagesPath: String, doughStatus: DoughStatus) = DoughStatusViewModel(
  id = doughStatus.id.toString(),
  recordedAt = doughStatus.recordedAt.atZone(ZoneId.of("Europe/Berlin")).format(formatter),
  recordedAtEpochSeconds = doughStatus.recordedAt.epochSecond,
  backlitImagePath = "$imagesPath/${doughStatus.backlitImageFile}",
  ambientImagePath = doughStatus.ambientImageFile?.let { "$imagesPath/$it" },
  doughData = AnalyzerResult.GlassPresent(100, 200, 400)
)
