package doh.grab

import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.shared.AnalyzerResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.Instant
import kotlin.system.measureTimeMillis

class AnalyzerLoop(
  private val analyzer: Analyzer,
  private val imageGrabber: ImageGrabber,
  private val doughStatusRepo: DoughStatusRepo,
  private val doughAnalysisRepo: DoughAnalysisRepo,
  private val grabFrequency: Duration
) {
  suspend fun run() = coroutineScope {
    while (isActive) {
      val duration = measureTimeMillis {
        println("Analyzer running ${Instant.now().epochSecond}")
        val images = imageGrabber.grabImages()

        val doughStatusId = doughStatusRepo.insert(
          backlitFilename = images.backlitImage.name,
          ambientFilename = images.ambientImage.name
        )

        val analyzerResult = analyzer.analyze(
          backlitFile = images.backlitImage,
          ambientFile = images.ambientImage
        )

        if (analyzerResult is AnalyzerResult.GlassPresent) {
          doughAnalysisRepo.insert(
            doughStatusId = doughStatusId,
            result = analyzerResult
          )
        }
      }

      delay(grabFrequency.toMillis() - duration)
    }
  }
}
