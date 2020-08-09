package doh.db

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class DoughStatusRepo(dbFile: File?) {
  private val db: DohDatabase

  init {
    val url =
      dbFile?.let { "jdbc:sqlite:${dbFile.absolutePath}" }
        ?: JdbcSqliteDriver.IN_MEMORY
    val driver = JdbcSqliteDriver(url)
    DohDatabase.Schema.create(driver)
    db = DohDatabase(driver)
  }

  fun getAll(): List<DoughStatus> {
    return db.doughStatusQueries.getAll(0, 100).executeAsList()
  }

  suspend fun insert(
    imageFileName: String,
    growth: Double
  ) = withContext(IO) {
    db.doughStatusQueries.insert(
      id = UUID.randomUUID().toString(),
      imageFileName = imageFileName,
      recordedAt = LocalDateTime.now().toString(),
      growth = growth
    )
  }

  suspend fun getLatestStatus(): DoughStatus? = withContext(IO) {
    db.doughStatusQueries.getLatest().executeAsOneOrNull()
  }
}
