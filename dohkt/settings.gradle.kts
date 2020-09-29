rootProject.name = "doh"

include(":app")
include(":app:dev")
include(":doh")
include(":db")
include(":grab")
include(":web")
include(":config")
include(":frontend")

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
