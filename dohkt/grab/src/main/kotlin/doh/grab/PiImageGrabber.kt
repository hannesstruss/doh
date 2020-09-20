package doh.grab

import doh.config.ImageDir
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import javax.inject.Inject

class PiImageGrabber @Inject constructor(
  @ImageDir private val imageDir: File,
  @Backlight private val backlight: Light
) : ImageGrabber {
  override suspend fun grabImage(): File = withContext(IO) {
    val filename = "doh-${Instant.now().epochSecond}.jpg"
    val tempFile = File(imageDir, "temp-$filename")
    val file = File(imageDir, filename)

    check(!file.exists()) { "Image file already exists: $file" }

    backlight.switch(on = true)
    "raspistill -o ${tempFile.absolutePath}".runCmd()
    backlight.switch(on = false)
    "convert ${tempFile.absolutePath} -rotate 180 -quality 50 -resize 50% ${file.absolutePath}".runCmd()

    file
  }
}
