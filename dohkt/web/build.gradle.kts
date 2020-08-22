import doh.meta.Deps
import doh.meta.Versions

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":db"))

  api(Deps.Ktor.serverNetty)
  implementation(Deps.Ktor.htmlBuilder)

  implementation(Deps.KotlinX.serializationCore)
}
