import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  kotlin("jvm") version "1.5.21" apply false
  kotlin("plugin.serialization") version "1.5.21" apply false
  id("com.squareup.sqldelight") version "1.5.1" apply false
}

subprojects {
  repositories {
    google()
    mavenCentral()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs += "-progressive"
      freeCompilerArgs += "-Xopt-in=io.ktor.locations.KtorExperimentalLocationsAPI"
    }
  }
}

group = "org.example"
version = "0.1-SNAPSHOT"

