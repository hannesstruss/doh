package doh.grab

import doh.config.ImageDir
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import javax.inject.Inject

class PiImageGrabber @Inject constructor(
  @ImageDir private val imageDir: File
) : ImageGrabber {
  override suspend fun grabImage(): File = withContext(IO) {
    val filename = "doh-${Instant.now().epochSecond}.jpg"
    val tempFile = File(imageDir, "temp-$filename")
    val file = File(imageDir, filename)

    check(!file.exists()) { "Image file already exists: $file" }

    "raspistill -o ${tempFile.absolutePath}".runShell()
    "convert ${tempFile.absolutePath} -quality 50 -resize 50% ${file.absolutePath}".runShell()

    file
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  private suspend fun String.runShell() = coroutineScope {
    val cmd: String = this@runShell

    println("PiImageGrabber running '$cmd' as UID ${System.getProperty("user.name")}")

    val process = Runtime.getRuntime().exec(cmd.split(" ").toTypedArray())
    val stdout = launch {
      process.inputStream.bufferedReader().use {
        while (true) {
          val line = it.readLine() ?: break
          println("Raspistill: $line")
        }
      }
    }
    val stderr = launch {
      process.errorStream.bufferedReader().use {
        while (true) {
          val line = it.readLine() ?: break
          println("Raspistill error: $line")
        }
      }
    }
    val resultCode = process.waitFor()
    println("${javaClass.name} got result $resultCode")
    stdout.join()
    stderr.join()

    if (resultCode != 0) {
      throw RuntimeException("'$cmd' exited with $resultCode")
    }
  }
}
