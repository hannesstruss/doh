package doh.grab

import doh.db.DoughStatusRepo
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
  private val grabFrequency: Duration
) {
  suspend fun run() = coroutineScope {
    while (isActive) {
      val duration = measureTimeMillis {
        println("Analyzer running ${Instant.now().epochSecond}")
        val images = imageGrabber.grabImages()
//      val analyzerResult = analyzer.analyze(images.backlitImage)
        doughStatusRepo.insert(
          backlitFilename = images.backlitImage.name,
          ambientFilename = images.ambientImage.name
        )
      }

      delay(grabFrequency.toMillis() - duration)
    }
  }
}
