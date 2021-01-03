import doh.meta.Deps

plugins {
  kotlin("jvm")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(project(":db"))
  implementation(project(":web:transport-model"))
  implementation(project(":shared"))

  api(Deps.Ktor.serverNetty)
  api(Deps.Ktor.serialization)
  implementation(Deps.Ktor.htmlBuilder)
}

// In development we'll run a Webpack dev server, so don't need to rebuild
// the react app to serve the backend.
if (!project.hasProperty("skipWeb") || project.property("skipWeb") != "true") {
  tasks.withType<Copy>().named("processResources") {
    from(project(":web:frontend").tasks.named("browserDistribution")) {
      into("doh/frontend")
    }
  }
}
