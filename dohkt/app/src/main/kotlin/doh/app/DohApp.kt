package doh.app

import doh.app.di.ImageDir
import doh.db.DoughStatusRepo
import doh.grab.Analyzer
import doh.grab.AnalyzerLoop
import doh.grab.ImageGrabber
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
  @ImageDir private val imageDir: File
) {
  fun run() {
    val repo = DoughStatusRepo(null)
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
