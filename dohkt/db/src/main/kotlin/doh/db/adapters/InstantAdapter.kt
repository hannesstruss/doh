package doh.db.adapters

import com.squareup.sqldelight.ColumnAdapter
import java.time.Instant

object InstantAdapter : ColumnAdapter<Instant, Long> {
  override fun decode(databaseValue: Long): Instant {
    return Instant.ofEpochMilli(databaseValue)
  }

  override fun encode(value: Instant): Long {
    return value.toEpochMilli()
  }
}
