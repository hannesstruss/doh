package doh

import doh.config.ImageDir
import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.grab.Ambient
import doh.grab.Analyzer
import doh.grab.AnalyzerLoop
import doh.grab.ImageGrabber
import doh.grab.Light
import doh.web.createWebApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.Duration
import javax.inject.Inject

class DohApp @Inject constructor(
  private val imageGrabber: ImageGrabber,
  private val analyzer: Analyzer,
  @ImageDir private val imageDir: File,
  private val doughStatusRepo: DoughStatusRepo,
  private val doughAnalysisRepo: DoughAnalysisRepo,
  @Ambient private val ambientLight: Light
) {
  fun run() {
    ambientLight.switch(on = false)

    val analyzerLoop = AnalyzerLoop(analyzer, imageGrabber, doughStatusRepo, doughAnalysisRepo, Duration.ofMinutes(10))

    val context = SupervisorJob()
    val scope = CoroutineScope(context)

    scope.launch { analyzerLoop.run() }

    createWebApp(
      doughStatusRepo = doughStatusRepo,
      imagesDir = imageDir,
      doughAnalysisRepo = doughAnalysisRepo
    ).start(wait = true)

    runBlocking {
      context.join()
    }
  }
}
