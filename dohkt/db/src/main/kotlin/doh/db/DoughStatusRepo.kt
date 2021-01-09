package doh.db

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID

class DoughStatusRepo(
  private val db: DohDatabase
) {
  suspend fun getAll(): List<DoughStatus> = withContext(IO) {
    db.doughStatusQueries.getAll(0, 100).executeAsList()
  }

  suspend fun getAllAfter(instant: Instant): List<DoughStatus> = withContext(IO) {
    db.doughStatusQueries.getAllAfter(instant).executeAsList()
  }

  suspend fun insert(
    backlitFilename: String,
    ambientFilename: String
  ): UUID = withContext(IO) {
    val id = UUID.randomUUID()
    db.doughStatusQueries.insert(
      id,
      Instant.now(),
      backlitFilename,
      ambientFilename
    )
    id
  }

  suspend fun getLatestStatus(): DoughStatus? = withContext(IO) {
    db.doughStatusQueries.getLatest().executeAsOneOrNull()
  }
}
