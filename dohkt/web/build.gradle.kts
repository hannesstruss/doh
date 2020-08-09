plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib"))
  api("io.ktor:ktor-server-netty:1.3.2")
  implementation("io.ktor:ktor-html-builder:1.3.2")
  implementation(project(":db"))
}
