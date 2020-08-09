import com.squareup.sqldelight.gradle.SqlDelightExtension

plugins {
  kotlin("jvm")
  id("com.squareup.sqldelight")
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")

  implementation("com.squareup.sqldelight:sqlite-driver:1.4.0")
}

configure<SqlDelightExtension> {
  database("DohDatabase") {
    packageName = "doh.db"
  }
}
