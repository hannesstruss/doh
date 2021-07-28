import doh.meta.Deps

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":db"))
  implementation(project(":web:transport-model"))
  implementation(project(":temperature"))
  implementation(project(":config"))
  implementation(project(":shared"))

  implementation(Deps.KotlinX.serializationJson)
  implementation(Deps.KotlinX.serializationCore)
  api(Deps.Ktor.serverNetty)
  api(Deps.Ktor.serialization)
  api(Deps.Ktor.auth)
  api(Deps.Ktor.authJwt)
  implementation(Deps.Ktor.htmlBuilder)
  implementation(Deps.Ktor.locations)
  api(Deps.Dagger.inject)
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
