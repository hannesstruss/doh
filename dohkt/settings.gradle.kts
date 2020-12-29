rootProject.name = "doh"

include(":shared")
include(":app")
include(":app:dev")
include(":doh")
include(":db")
include(":grab")
include(":web")
include(":web:api")
include(":web:frontend")
include(":web:transport-model")
include(":config")

pluginManagement {
  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "org.jetbrains.kotlin.plugin.serialization" -> {
          useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
        }
      }
    }
  }
}
