package doh.app

import doh.app.di.DaggerDevDohComponent

fun main() {
  val component = DaggerDevDohComponent.factory().create()
  val app = component.dohApp()
  app.run()
}
