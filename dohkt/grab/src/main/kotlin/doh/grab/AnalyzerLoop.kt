package doh.grab

import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.shared.AnalyzerResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class AnalyzerLoop
@Inject constructor(
  private val analyzer: Analyzer,
  private val imageGrabber: ImageGrabber,
  private val doughStatusRepo: DoughStatusRepo,
  private val doughAnalysisRepo: DoughAnalysisRepo,
) {
  private val grabFrequency = Duration.ofMinutes(10)

  suspend fun run() = coroutineScope {
    while (isActive) {
      val duration = measureTimeMillis {
        println("${AnalyzerLoop::class.java.simpleName} running ${Instant.now().epochSecond}")
        val images = imageGrabber.grabImages()

        val doughStatusId = doughStatusRepo.insert(
          backlitFilename = images.backlitImage.name,
          ambientFilename = images.ambientImage.name
        )

        try {
          analyzeImages(images, doughStatusId)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }

      delay(grabFrequency.toMillis() - duration)
    }
  }

  private suspend fun analyzeImages(images: ImageGrabber.Result, doughStatusId: UUID) {
    val analyzerResult = try {
      analyzer.analyze(
        backlitFile = images.backlitImage,
        ambientFile = images.ambientImage
      )
    } catch (e: Exception) {
      throw RuntimeException("Running analyzer for DoughStatus[$doughStatusId] failed", e)
    }

    if (analyzerResult is AnalyzerResult.GlassPresent) {
      doughAnalysisRepo.insert(
        doughStatusId = doughStatusId,
        result = analyzerResult
      )
    }
  }
}
