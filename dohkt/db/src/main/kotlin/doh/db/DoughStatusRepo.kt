package doh.db

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class DoughStatusRepo(dbFile: File?) {
  private val db: DohDatabase

  init {
    val url = dbFile?.let { "jdbc:sqlite:${dbFile.absolutePath}" }
      ?: JdbcSqliteDriver.IN_MEMORY
    val driver = JdbcSqliteDriver(url)
    DohDatabase.Schema.create(driver)
    db = DohDatabase(driver)

    repeat(5) { insertDummy() }
  }

  private fun insertDummy() {
    db.doughStatusQueries.insert(
      UUID.randomUUID().toString(),
      1,
      LocalDateTime.now().toString(),
      1.2 + Math.random() * 0.3
    )
  }

  fun getAll(): List<DoughStatus> {
    return db.doughStatusQueries.getAll(0, 100).executeAsList()
  }
}
