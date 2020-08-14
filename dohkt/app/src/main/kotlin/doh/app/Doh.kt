package doh.app

import doh.db.DoughStatusRepo
import doh.grab.Analyzer
import doh.grab.AnalyzerLoop
import doh.grab.FakeImageGrabber
import doh.web.createWebApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.nio.file.Files

fun main() {
  val repo = DoughStatusRepo(null)
  val tempImageDir = Files.createTempDirectory("doh-fakes").toFile()
  val analyzerLoop = AnalyzerLoop(Analyzer(), FakeImageGrabber(tempImageDir), repo, 10_000)

  val context = SupervisorJob()
  val scope = CoroutineScope(context)

  scope.launch { analyzerLoop.run() }

  createWebApp(
    repo = repo,
    imagesDir = tempImageDir
  ).start(wait = true)

  runBlocking {
    context.join()
  }
}
