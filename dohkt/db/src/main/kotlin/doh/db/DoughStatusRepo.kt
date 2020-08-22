package doh.db

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.time.Instant

class DoughStatusRepo(
  private val db: DohDatabase
) {
  suspend fun getAll(): List<DoughStatus> = withContext(IO) {
    db.doughStatusQueries.getAll(0, 100).executeAsList()
  }

  suspend fun getAllAfter(instant: Instant): List<DoughStatus> = withContext(IO) {
    db.doughStatusQueries.getAllAfter(instant).executeAsList()
  }

  suspend fun insert(status: DoughStatus) = withContext(IO) {
    db.doughStatusQueries.insert(
      status.id,
      status.recordedAt,
      status.imageFile,
      status.growth
    )
  }

  suspend fun getLatestStatus(): DoughStatus? = withContext(IO) {
    db.doughStatusQueries.getLatest().executeAsOneOrNull()
  }
}
