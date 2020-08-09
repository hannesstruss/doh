import doh.meta.Deps

plugins {
  kotlin("jvm")
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation(Deps.KotlinX.coroutinesCore)

  implementation(project(":db"))
}
