import doh.meta.Deps

plugins {
  kotlin("jvm")
  application
}

application {
  mainClassName = "doh.app.DohKt"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":web"))
  implementation(project(":db"))
  implementation(project(":grab"))

  implementation(Deps.Slf4jSimple)
}
