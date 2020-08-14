package doh.db.adapters

import com.squareup.sqldelight.ColumnAdapter
import java.util.UUID

object UUIDAdapter : ColumnAdapter<UUID, String> {
  override fun decode(databaseValue: String): UUID {
    return UUID.fromString(databaseValue)
  }

  override fun encode(value: UUID): String {
    return value.toString()
  }
}
