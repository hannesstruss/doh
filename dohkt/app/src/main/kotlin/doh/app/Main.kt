package doh.app

import doh.DohApp
import doh.dev.DevDohComponent
import doh.di.ProdDohComponent

fun main(args: Array<String>) {
  val app: DohApp = if (args.firstOrNull() == "prod") {
    val component = ProdDohComponent()
    component.dohApp()
  } else {
    val component = DevDohComponent()
    component.dohApp()
  }
  app.run()
}
