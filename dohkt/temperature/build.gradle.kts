import doh.meta.Deps

plugins {
  kotlin("jvm")
}

dependencies {
  implementation(Deps.KotlinX.coroutinesCore)
  implementation(Deps.Dagger.inject)
  implementation(project(":db"))

  testImplementation(platform("org.junit:junit-bom:5.7.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
