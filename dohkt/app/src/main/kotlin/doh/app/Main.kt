package doh.app

import doh.DohApp
import doh.dev.DevDohComponent
import doh.di.PiDohComponent

private const val EnvTypeEnvVar = "DOH_ENVTYPE"

fun main() {
  val envType = System.getenv(EnvTypeEnvVar)

  val app: DohApp = when (envType) {
    "pi" -> {
      val component = PiDohComponent()
      component.dohApp()
    }
    "dev" -> {
      val component = DevDohComponent()
      component.dohApp()
    }
    else -> throw RuntimeException("'$envType' is not a valid value for $EnvTypeEnvVar")
  }
  app.run()
}
