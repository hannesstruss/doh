package doh.grab

import doh.db.DoughStatusRepo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Instant

class AnalyzerLoop(
  private val analyzer: Analyzer,
  private val imageGrabber: ImageGrabber,
  private val doughStatusRepo: DoughStatusRepo,
  private val grabFrequencyMs: Long
) {
  suspend fun run() = coroutineScope {
    while (isActive) {
      println("Analyzer running ${Instant.now().epochSecond}")
      val image = imageGrabber.grabImage()
      val status = analyzer.analyze(image)
      doughStatusRepo.insert(status)
      delay(grabFrequencyMs)
    }
  }
}
