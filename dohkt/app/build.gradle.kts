import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("kapt")
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

  implementation(Deps.Dagger.dagger)
  kapt(Deps.Dagger.compiler)

  implementation(Deps.Slf4jSimple)
}
