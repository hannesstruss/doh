package doh.db

import doh.shared.AnalyzerResult
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.UUID

class DoughAnalysisRepo(
  private val db: DohDatabase
) {
  suspend fun insert(
    doughStatusId: UUID,
    result: AnalyzerResult.GlassPresent
  ) = withContext(IO) {
    db.doughAnalysisQueries.insert(
      id = UUID.randomUUID(),
      doughStatusId = doughStatusId,
      glassBottomY = result.glassBottomY.toLong(),
      rubberBandY = result.rubberBandY.toLong(),
      doughLevelY = result.doughLevelY.toLong()
    )
  }

  suspend fun forDoughStatus(doughStatusId: UUID): DoughAnalysis? = withContext(IO) {
    db.doughAnalysisQueries.getForDoughStatus(doughStatusId).executeAsOneOrNull()
  }
}
