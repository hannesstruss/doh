import doh.meta.Deps.Dagger
import doh.meta.Deps.KotlinX

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

dependencies {
  api(Dagger.inject)
  api(KotlinX.serializationCore)
}
