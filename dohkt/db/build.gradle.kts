import com.squareup.sqldelight.gradle.SqlDelightExtension

plugins {
  kotlin("jvm")
  id("com.squareup.sqldelight")
}

dependencies {
  implementation(kotlin("stdlib"))
}

configure<SqlDelightExtension> {
  database("DohDatabase") {
    packageName = "doh.db"
  }
}
