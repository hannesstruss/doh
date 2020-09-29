package doh.grab

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import javax.inject.Inject

class PiCamera @Inject constructor() : Camera {
  /**
   * Grab an image from the Raspberry Pi camera and place it into [directory].
   * Use [classifier] as part of the filename.
   */
  override suspend fun grab(directory: File, classifier: String): File = withContext(IO) {
    check(directory.isDirectory) { "$directory is not a directory" }

    val filename = "$classifier-${Instant.now().epochSecond}.jpg"
    val tempFile = directory.resolve("temp-$filename")
    val resultFile = directory.resolve(filename)

    "raspistill -o ${tempFile.absolutePath}".runCmd()
    "convert ${tempFile.absolutePath} -rotate 180 -quality 50 -resize 50% ${resultFile.absolutePath}".runCmd()

    tempFile.delete()

    resultFile
  }
}
