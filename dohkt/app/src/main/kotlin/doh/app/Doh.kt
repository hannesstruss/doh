package doh.app

import doh.app.di.DaggerDohComponent
import java.nio.file.Files

fun main() {
  val tempImageDir = Files.createTempDirectory("doh-fakes").toFile()

  val component = DaggerDohComponent.factory().create(
    imageDir = tempImageDir
  )
  val app = component.dohApp()
  app.run()
}
