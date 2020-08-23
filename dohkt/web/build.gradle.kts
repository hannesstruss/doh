import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(project(":db"))

  api(Deps.Ktor.serverNetty)
  implementation(Deps.Ktor.htmlBuilder)

  implementation(Deps.KotlinX.serializationCore)
}
