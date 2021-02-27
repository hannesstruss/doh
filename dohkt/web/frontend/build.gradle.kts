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

  implementation("org.jetbrains:kotlin-react:17.0.1-pre.148-kotlin-1.4.30")
  implementation("org.jetbrains:kotlin-react-dom:17.0.1-pre.148-kotlin-1.4.30")
  implementation(npm("react", "17.0.1"))
  implementation(npm("react-dom", "17.0.1"))

  //Kotlin Styled (chapter 3)
  implementation("org.jetbrains:kotlin-styled:5.2.1-pre.148-kotlin-1.4.30")
  implementation(npm("styled-components", "~5.2.1"))
  implementation(npm("inline-style-prefixer", "~6.0.0"))

  implementation(Deps.KotlinX.coroutinesCore)

  implementation(Deps.KotlinX.serializationJson)
  implementation(Deps.Ktor.clientSerialization)
  implementation(Deps.Ktor.clientJsonJs)
}
