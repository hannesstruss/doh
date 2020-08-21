package doh.db

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import doh.db.adapters.InstantAdapter
import doh.db.adapters.UUIDAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.util.UUID

class DoughStatusRepo(dbFile: File?) {
  private val db: DohDatabase

  init {
    val url =
      dbFile?.let { "jdbc:sqlite:${dbFile.absolutePath}" }
        ?: JdbcSqliteDriver.IN_MEMORY
    val driver = JdbcSqliteDriver(url)
    DohDatabase.Schema.create(driver)

    val doughStatusAdapter = DoughStatus.Adapter(
      recordedAtAdapter = InstantAdapter,
      idAdapter = UUIDAdapter
    )

    db = DohDatabase(driver, doughStatusAdapter)
  }

  fun getAll(): List<DoughStatus> {
    return db.doughStatusQueries.getAll(0, 100).executeAsList()
  }

  suspend fun insert(
    growth: Double
  ) = withContext(IO) {
    db.doughStatusQueries.insert(
      id = UUID.randomUUID(),
      recordedAt = Instant.now(),
      growth = growth
    )
  }

  suspend fun insert(status: DoughStatus) {
    db.doughStatusQueries.insert(status.id, status.recordedAt, status.growth)
  }

  suspend fun getLatestStatus(): DoughStatus? = withContext(IO) {
    db.doughStatusQueries.getLatest().executeAsOneOrNull()
  }
}