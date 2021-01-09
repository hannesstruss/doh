import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("kapt")
  application
}

application {
  mainClass.set("doh.app.MainKt")
  applicationDefaultJvmArgs = listOf(
    "-Dkotlinx.coroutines.debug=on"
  )
}

dependencies {
  implementation(project(":app:dev"))
  implementation(project(":doh"))

  implementation(Deps.Dagger.dagger)
  kapt(Deps.Dagger.compiler)

  implementation(Deps.logback)
}
