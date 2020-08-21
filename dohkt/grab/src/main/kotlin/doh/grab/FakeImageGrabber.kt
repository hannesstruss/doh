package doh.grab

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime

class FakeImageGrabber(
  private val imageDir: File
) : ImageGrabber {
  private var imageIndex = 0

  override suspend fun grabImage(): File = withContext(Dispatchers.IO) {
    val timestamp = LocalDateTime.now().toString()
    val targetFile = imageDir.resolve("$timestamp.jpg")

    check(!targetFile.exists()) { "You're trying to grab images too fast!" }

    val resourceStream = javaClass.getResourceAsStream("/sourdough-dummy-${imageIndex + 1}.jpg")
    resourceStream.use { inStream ->
      targetFile.outputStream().use { outStream ->
        inStream.copyTo(outStream)
      }
    }

    imageIndex = (imageIndex + 1) % 5

    targetFile
  }
}
