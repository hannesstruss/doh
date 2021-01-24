package doh.db

import doh.db.mappers.toAnalyzerResult
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

  suspend fun forDoughStatus(doughStatusId: UUID): AnalyzerResult.GlassPresent? = withContext(IO) {
    db.doughAnalysisQueries
      .getForDoughStatus(doughStatusId)
      .executeAsOneOrNull()
      ?.toAnalyzerResult()
  }

  suspend fun forDoughStatuses(doughStatusIds: List<UUID>): Map<UUID, AnalyzerResult> =
    withContext(IO) {
      val analyses = doughStatusIds
        .mapNotNull { id ->
          forDoughStatus(id)?.let { id to it }
        }
      val result = mutableMapOf<UUID, AnalyzerResult>()
      for (pair in analyses) {
        result[pair.first] = pair.second
      }
      result
    }
}
