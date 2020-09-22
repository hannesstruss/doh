import doh.meta.Deps.Dagger

plugins {
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  implementation(Dagger.dagger)
  kapt(Dagger.compiler)

  api(project(":web"))
  api(project(":db"))
  api(project(":grab"))
  api(project(":config"))
}
