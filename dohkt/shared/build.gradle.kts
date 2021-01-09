import doh.meta.Deps

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  js {
    browser()
  }
  jvm()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(Deps.KotlinX.serializationCore)
      }
    }
  }
}
