package doh.db

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DoughStatusRepo(
  private val db: DohDatabase
) {
  fun getAll(): List<DoughStatus> {
    return db.doughStatusQueries.getAll(0, 100).executeAsList()
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
