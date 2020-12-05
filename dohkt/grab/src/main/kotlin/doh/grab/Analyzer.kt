package doh.grab

import doh.config.AnalyzerHost
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import java.io.File
import javax.inject.Inject

class Analyzer
@Inject constructor(
  @AnalyzerHost private val analyzerHost: String
) {
  private val httpClient = HttpClient()

  suspend fun analyze(backlitFile: File, ambientFile: File): AnalyzerResult {
    val result: String = httpClient.get(analyzerHost)
    println(result)

//    val cmd = "$scriptCommand analyze --ambient=${ambientFile.absolutePath} --backlit=${backlitFile.absolutePath}"
//    cmd.runCmd()

    return AnalyzerResult(Math.random())
  }
}
