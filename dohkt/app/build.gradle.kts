plugins {
  kotlin("jvm")
  application
}

application {
  mainClassName = "doh.app.DohKt"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":web"))
  implementation(project(":db"))

  implementation("org.slf4j:slf4j-simple:1.7.30")
}
