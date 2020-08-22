package doh.app

import doh.app.di.DaggerDevDohComponent
import java.nio.file.Files

fun main() {
  val tempImageDir = Files.createTempDirectory("doh-fakes").toFile()

  val component = DaggerDevDohComponent.factory().create(
    imageDir = tempImageDir
  )
  val app = component.dohApp()
  app.run()
}
