import doh.meta.Deps

plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(project(":db"))
  implementation(project(":web:transport-model"))

  api(Deps.Ktor.serverNetty)
  api(Deps.Ktor.serialization)
  implementation(Deps.Ktor.htmlBuilder)
}
