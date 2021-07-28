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
  mavenCentral()
//  maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
}

// TODO unify deps in Deps class and update to latest versions
dependencies {
  implementation(project(":web:transport-model"))

  implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.222-kotlin-1.5.21")
  implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.222-kotlin-1.5.21")
  implementation(npm("react", "17.0.2"))
  implementation(npm("react-dom", "17.0.2"))

  //Kotlin Styled (chapter 3)
  implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.0-pre.222-kotlin-1.5.21")
  implementation(npm("styled-components", "~5.3.0"))
  implementation(npm("inline-style-prefixer", "~6.0.0"))

  implementation(Deps.KotlinX.coroutinesCore)

  implementation(Deps.KotlinX.serializationJson)
  implementation(Deps.Ktor.clientSerialization)
  implementation(Deps.Ktor.clientJsonJs)
}

val configSourceDir = File(project.buildDir, "buildConfig")
val generateConfig = project.tasks.register("generateConfig") {
  doLast {
    configSourceDir.mkdirs()
    val f = File(configSourceDir, "config.kt")
    val value = project.properties.getOrDefault("apiHost", null)
      ?.let { "\"$it\"" }
      ?: "kotlinx.browser.window.location.origin"
    f.writeText("val BackendHost: String = $value")
  }
}

kotlin.sourceSets {
  val main by getting {
    this.kotlin.srcDir(configSourceDir.path)
  }
}

project.tasks.named("compileKotlinJs").configure {
  dependsOn(generateConfig)
}
