import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.72" apply false
}

subprojects {
  repositories {
    mavenCentral()
    jcenter()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs = listOf("-progressive")
    }
  }
}

group = "org.example"
version = "0.1-SNAPSHOT"
