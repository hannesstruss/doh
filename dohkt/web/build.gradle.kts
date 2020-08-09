import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  application
}

application {
  mainClassName = "doh.web.AppKt"
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib"))
  api("io.ktor:ktor-server-netty:1.3.2")
}
