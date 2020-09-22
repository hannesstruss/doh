package doh

import doh.config.ImageDir
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
  @ImageDir private val imageDir: File,
  private val repo: DoughStatusRepo,
  @Ambient private val ambientLight: Light
) {
  fun run() {
    ambientLight.switch(on = false)

    val analyzerLoop = AnalyzerLoop(Analyzer(), imageGrabber, repo, Duration.ofMinutes(10))

    val context = SupervisorJob()
    val scope = CoroutineScope(context)

    scope.launch { analyzerLoop.run() }

    createWebApp(
      repo = repo,
      imagesDir = imageDir
    ).start(wait = true)

    runBlocking {
      context.join()
    }
  }
}
