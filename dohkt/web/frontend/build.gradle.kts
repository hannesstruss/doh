import doh.meta.Deps

plugins {
  kotlin("js")
}

kotlin {
  js {
    browser()
    binaries.executable()
  }
}

repositories {
  maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}

// TODO unify deps in Deps class and update to latest versions
dependencies {
  implementation(kotlin("stdlib-js"))

  implementation(project(":web:transport-model"))

  implementation("org.jetbrains:kotlin-react:16.13.1-pre.110-kotlin-1.4.0")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.110-kotlin-1.4.0")
  implementation(npm("react", "16.13.1"))
  implementation(npm("react-dom", "16.13.1"))

  //Kotlin Styled (chapter 3)
  implementation("org.jetbrains:kotlin-styled:1.0.0-pre.110-kotlin-1.4.0")
  implementation(npm("styled-components", "~5.2.0"))
  implementation(npm("inline-style-prefixer", "~6.0.0"))

  //Coroutines (chapter 8)
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

  implementation(Deps.KotlinX.serializationJson)
  implementation(Deps.Ktor.clientSerialization)
  implementation(Deps.Ktor.clientJsonJs)
}
