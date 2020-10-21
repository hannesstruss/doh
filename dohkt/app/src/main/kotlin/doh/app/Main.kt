package doh.app

import doh.DohApp
import doh.config.Config
import doh.dev.DevDohComponent
import doh.di.ProdDohComponent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.io.File

fun main(args: Array<String>) {
  val cfg = Json.decodeFromString<Config>(File("/etc/dohrc").readText())

  val app: DohApp = if (args.firstOrNull() == "prod") {
    val component = ProdDohComponent()
    component.dohApp()
  } else {
    val component = DevDohComponent(cfg)
    component.dohApp()
  }
  app.run()
}
