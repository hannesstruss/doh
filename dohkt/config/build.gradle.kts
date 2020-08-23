import doh.meta.Deps.Dagger

plugins {
  kotlin("jvm")
}

dependencies {
  api(Dagger.inject)
}
