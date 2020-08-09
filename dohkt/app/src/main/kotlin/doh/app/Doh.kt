package doh.app

import doh.db.DoughStatusRepo
import doh.grab.FakeImageGrabber
import doh.web.createWebApp
import kotlinx.coroutines.runBlocking
import java.nio.file.Files

fun main() {
  val repo = DoughStatusRepo(null)
  val tempImageDir = Files.createTempDirectory("doh-fakes").toFile()
  val imageGrabber = FakeImageGrabber(tempImageDir)
  runBlocking {
    val grabbed = imageGrabber.grabImage()
    repo.insert(grabbed.name, 1.2)
    println(grabbed.absolutePath)
  }

  createWebApp(
    repo = repo,
    imagesDir = tempImageDir
  ).start(wait = true)
}
