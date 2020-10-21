package doh.dev

import doh.config.ImageDir
import doh.grab.Camera
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class FakeCamera @Inject constructor(
  @ImageDir private val imageDir: File
) : Camera {
  private var imageIndex = 0

  override suspend fun grab(directory: File, classifier: String): File = withContext(Dispatchers.IO) {
    val timestamp = LocalDateTime.now().toString()
    val targetFile = imageDir.resolve("$timestamp.jpg")

    check(!targetFile.exists()) { "You're trying to grab images too fast!" }

    val resourceStream = javaClass.getResourceAsStream("/doh/dev/$classifier-dummy-${imageIndex + 1}.jpg")
    resourceStream.use { inStream ->
      targetFile.outputStream().use { outStream ->
        inStream.copyTo(outStream)
      }
    }

    imageIndex = (imageIndex + 1) % 5

    targetFile
  }
}
