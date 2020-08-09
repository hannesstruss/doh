import com.squareup.sqldelight.gradle.SqlDelightExtension
import doh.meta.Deps

plugins {
  kotlin("jvm")
  id("com.squareup.sqldelight")
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation(Deps.KotlinX.coroutinesCore)

  implementation(Deps.SqlDelightDriver)
}

configure<SqlDelightExtension> {
  database("DohDatabase") {
    packageName = "doh.db"
  }
}
