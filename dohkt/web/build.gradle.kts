import doh.meta.Deps

plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib"))
  api(Deps.Ktor.serverNetty)
  implementation(Deps.Ktor.htmlBuilder)
  implementation(project(":db"))
}
