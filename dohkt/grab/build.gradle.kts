import doh.meta.Deps

plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Deps.KotlinX.coroutinesCore)

  implementation(project(":db"))
  implementation(project(":config"))

  api(Deps.Dagger.inject)
}
