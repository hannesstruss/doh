rootProject.name = "doh"

include(":app")
include(":db")
include(":grab")
include(":web")

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
