plugins {
  kotlin("jvm")
  kotlin("kapt")
}

dependencies {
  implementation(project(":doh"))

  implementation(doh.meta.Deps.Dagger.dagger)
  kapt(doh.meta.Deps.Dagger.compiler)
}
