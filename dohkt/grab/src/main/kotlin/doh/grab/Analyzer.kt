package doh.grab

import doh.config.AnalyzerScriptCommand
import doh.db.DoughStatus
import java.io.File
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class Analyzer
@Inject constructor(
  @AnalyzerScriptCommand private val scriptCommand: String
) {
  suspend fun analyze(backlitFile: File, ambientFile: File): AnalyzerResult {
    val cmd = "$scriptCommand analyze --ambient=${ambientFile.absolutePath} --backlit=${backlitFile.absolutePath}"
    cmd.runCmd()

    return AnalyzerResult(Math.random())
  }
}
