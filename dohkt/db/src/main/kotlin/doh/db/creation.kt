package doh.db

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import doh.db.adapters.InstantAdapter
import doh.db.adapters.UUIDAdapter
import java.io.File

fun createDb(url: String): DohDatabase {
  val driver = JdbcSqliteDriver(url)
  val schemaManager = SchemaManager(driver)
  schemaManager.createOrMigrate()

  return DohDatabase(
    driver,
    DoughStatusAdapter = DoughStatus.Adapter(
      recordedAtAdapter = InstantAdapter,
      idAdapter = UUIDAdapter
    )
  )
}

class SchemaManager(
  private val driver: JdbcSqliteDriver
) {
  fun createOrMigrate() {
    val schema = DohDatabase.Schema
    val currentVersion = getVersion()

    enableForeignKeys()

    if (currentVersion == 0) {
      schema.create(driver)
      setVersion(schema.version)
      println("Schema created")
    } else {
      val latestSchemaVersion = schema.version
      if (latestSchemaVersion > currentVersion) {
        println("Current schema: $currentVersion Latest: $latestSchemaVersion - Migrating!")
        schema.migrate(driver, currentVersion, latestSchemaVersion)
        setVersion(latestSchemaVersion)
        println("Migrated from $currentVersion to $latestSchemaVersion")
      } else {
        println("Current schema: $currentVersion. No migration needed.")
      }
    }
  }

  private fun enableForeignKeys() {
    driver.execute(null, "PRAGMA foreign_keys = ON;", 0, null)
  }

  private fun getVersion(): Int {
    val cursor = driver.executeQuery(null, "PRAGMA user_version;", 0, null)
    return checkNotNull(cursor.getLong(0)?.toInt()) { "user_version was null" }
  }

  private fun setVersion(version: Int) {
    driver.execute(null, "PRAGMA user_version = $version;", 0, null)
  }
}

fun createFilesystemDb(file: File): DohDatabase {
  val url = "jdbc:sqlite:${file.absolutePath}"
  return createDb(url)
}
