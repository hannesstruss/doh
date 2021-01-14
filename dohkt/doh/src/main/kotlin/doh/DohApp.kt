package doh

import doh.config.ImageDir
import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.db.TemperatureReadingRepo
import doh.grab.Ambient
import doh.grab.Analyzer
import doh.grab.AnalyzerLoop
import doh.grab.ImageGrabber
import doh.grab.Light
import doh.temperature.TemperatureReaderLoop
import doh.web.DohWebApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.Duration
import javax.inject.Inject

class DohApp @Inject constructor(
  private val webApp: DohWebApp,
  private val analyzerLoop: AnalyzerLoop,
  @Ambient private val ambientLight: Light,
  private val temperatureReaderLoop: TemperatureReaderLoop
) {
  fun run() {
    ambientLight.switch(on = false)

//    val analyzerLoop = AnalyzerLoop(analyzer, imageGrabber, doughStatusRepo, doughAnalysisRepo, Duration.ofMinutes(10))

    val context = SupervisorJob()
    val scope = CoroutineScope(context)

    scope.launch { analyzerLoop.run() }
    scope.launch { temperatureReaderLoop.run() }

    webApp.createWebApp().start(wait = true)

    runBlocking {
      context.join()
    }
  }
}
