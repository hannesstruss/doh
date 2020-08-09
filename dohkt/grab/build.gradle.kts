plugins {
  kotlin("jvm")
}

dependencies {
  implementation(kotlin("stdlib"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")

  implementation(project(":db"))
}
