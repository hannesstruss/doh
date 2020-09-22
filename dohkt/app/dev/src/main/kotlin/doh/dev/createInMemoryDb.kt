package doh.dev

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import doh.db.DohDatabase
import doh.db.createDb

fun createFakeDb(): DohDatabase {
  val url = JdbcSqliteDriver.IN_MEMORY + "/tmp/doh.sqlite"
  val db = createDb(url)

//  for (n in 1 until 5) {
//    db.doughStatusQueries.insert(
//      UUID.randomUUID(),
//      Instant.now().minus((n - 1) * 30 + 5L, ChronoUnit.MINUTES),
//      "sourdough-dummy-$n.jpg",
//      n * 0.1
//    )
//  }

  return db
}
