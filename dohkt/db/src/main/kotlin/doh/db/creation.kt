package doh.db

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import doh.db.adapters.InstantAdapter
import doh.db.adapters.UUIDAdapter
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

private fun createDb(url: String, createSchema: Boolean): DohDatabase {
  val driver = JdbcSqliteDriver(url)
  if (createSchema) {
    DohDatabase.Schema.create(driver)
  }

  val doughStatusAdapter = DoughStatus.Adapter(
    recordedAtAdapter = InstantAdapter,
    idAdapter = UUIDAdapter
  )

  return DohDatabase(driver, doughStatusAdapter)
}

fun createInMemoryDb(): DohDatabase {
  val url = JdbcSqliteDriver.IN_MEMORY
  val db = createDb(url, createSchema = true)

  for (n in 1 until 5) {
    db.doughStatusQueries.insert(
      UUID.randomUUID(),
      Instant.now().minus((n - 1) * 30 + 5L, ChronoUnit.MINUTES),
      "sourdough-dummy-$n.jpg",
      n * 0.1
    )
  }

  return db
}

fun createFilesystemDb(file: File): DohDatabase {
  val url = "jdbc:sqlite:${file.absolutePath}"
  val db = createDb(url, false)
  return db
}
