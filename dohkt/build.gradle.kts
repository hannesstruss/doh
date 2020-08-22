import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google()
    mavenCentral()
    jcenter()
  }
}

plugins {
  kotlin("jvm") version "1.4.0" apply false
  id("com.squareup.sqldelight") version "1.4.0" apply false
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

tasks.register("deployToPi") {
  group = "deploy"
  dependsOn(":app:distZip")
  doLast {
    exec {
      standardOutput = System.out
      val distZipProvider = project(":app").tasks.named<Zip>("distZip")
      val archivePath = distZipProvider.get().archiveFile.get().asFile.absolutePath

      commandLine = listOf("bash", "gradle/deploypi.sh", archivePath)
    }
  }
}
