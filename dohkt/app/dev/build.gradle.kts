import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  implementation(project(":doh"))

  implementation(Deps.Dagger.dagger)
  kapt(Deps.Dagger.compiler)

  implementation(Deps.SqlDelightDriver)
}
