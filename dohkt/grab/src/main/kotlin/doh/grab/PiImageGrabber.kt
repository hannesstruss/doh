package doh.grab

import doh.config.ImageDir
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject

class PiImageGrabber @Inject constructor(
  @ImageDir private val imageDir: File
): ImageGrabber {
  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun grabImage(): File = withContext(IO) {
    val filename = "doh-${Instant.now().epochSecond}.jpg"
    val file = File(imageDir, filename)

    check(!file.exists()) { "Image file already exists: $file" }

    val cmd = "raspistill -o ${file.absolutePath}"
    println("${javaClass.name} running '$cmd' as UID ${System.getProperty("user.name")}")
    val process = Runtime.getRuntime()
      .exec(cmd.split(" ").toTypedArray())
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
      throw RuntimeException("raspistill exited with $resultCode")
    }

    file
  }
}
