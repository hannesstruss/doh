package doh.db

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import doh.db.adapters.InstantAdapter
import doh.db.adapters.UUIDAdapter
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

fun createInMemoryDb(): DohDatabase {
  val url = JdbcSqliteDriver.IN_MEMORY
  val driver = JdbcSqliteDriver(url)
  DohDatabase.Schema.create(driver)

  val doughStatusAdapter = DoughStatus.Adapter(
    recordedAtAdapter = InstantAdapter,
    idAdapter = UUIDAdapter
  )

  val db = DohDatabase(driver, doughStatusAdapter)

  for (n in 1 until 5) {
    db.doughStatusQueries.insert(
      UUID.randomUUID(),
      Instant.now().minus(n * 5 + 5L, ChronoUnit.MINUTES),
      "sourdough-dummy-$n.jpg",
      n * 0.1
    )
  }

  return db
}
