import com.squareup.sqldelight.gradle.SqlDelightExtension
import doh.meta.Deps

plugins {
  kotlin("jvm")
  id("com.squareup.sqldelight")
}

dependencies {
  implementation(project(":shared"))

  implementation(Deps.KotlinX.coroutinesCore)
  implementation(Deps.Dagger.inject)

  implementation(Deps.SqlDelightDriver)
}

configure<SqlDelightExtension> {
  database("DohDatabase") {
    packageName = "doh.db"
    schemaOutputDirectory = file("src/main/sqldelight/databases")
  }
}
