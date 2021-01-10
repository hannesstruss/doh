package doh.db

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class TemperatureReadingRepo
@Inject constructor(
  private val db: DohDatabase
) {
  suspend fun insert(centigrades: Double) = withContext(IO) {
    db.temperatureReadingQueries.insert(
      id = UUID.randomUUID(),
      measuredAt = Instant.now(),
      centigrades = centigrades
    )
  }

  suspend fun getLatest(): Double? = withContext(IO) {
    db.temperatureReadingQueries.getLatest().executeAsOneOrNull()?.centigrades
  }
}
