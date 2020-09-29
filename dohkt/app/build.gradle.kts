import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("kapt")
  application
}

application {
  mainClassName = "doh.app.MainKt"
}

dependencies {
  implementation(project(":app:dev"))
  implementation(project(":doh"))

  implementation(Deps.Dagger.dagger)
  kapt(Deps.Dagger.compiler)

  implementation(Deps.logback)
}
