package doh.app

import doh.app.di.DaggerDevDohComponent
import doh.app.di.DaggerProdDohComponent

fun main(args: Array<String>) {
  val app: DohApp = if (args.firstOrNull() == "prod") {
    val component = DaggerProdDohComponent.create()
    component.dohApp()
  } else {
    val component = DaggerDevDohComponent.factory().create()
    component.dohApp()
  }
  app.run()
}
