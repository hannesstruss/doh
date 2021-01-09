import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

dependencies {
  implementation(Deps.KotlinX.coroutinesCore)

  implementation(project(":db"))
  implementation(project(":config"))
  implementation(project(":shared"))

  api(Deps.Dagger.inject)

  implementation(Deps.KotlinX.serializationJson)
  implementation(Deps.Ktor.clientCio)
  implementation(Deps.Ktor.clientSerialization)
}
