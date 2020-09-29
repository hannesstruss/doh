package doh.dev

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import doh.db.DohDatabase
import doh.db.createDb
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

fun createFakeDb(): DohDatabase {
  val url = JdbcSqliteDriver.IN_MEMORY + "/tmp/doh.sqlite"
  val db = createDb(url)

  for (n in 1 until 5) {
    db.doughStatusQueries.insert(
      UUID.randomUUID(),
      Instant.now().minus((n - 1) * 30 + 5L, ChronoUnit.MINUTES),
      "sourdough-dummy-$n.jpg",
      "sourdough-dummy-$n.jpg",
    )
  }

  return db
}
