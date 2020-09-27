package doh.grab

import doh.db.DoughStatus
import java.io.File
import java.time.Instant
import java.util.UUID

class Analyzer {
  suspend fun analyze(imageFile: File): AnalyzerResult {
    return AnalyzerResult(Math.random())
  }
}
