package doh.grab

import doh.config.ImageDir
import java.io.File
import javax.inject.Inject

class ImageGrabber @Inject constructor(
  @ImageDir private val imageDir: File,
  private val camera: Camera,
  @Backlight private val backlight: Light,
  @Ambient private val ambientLight: Light
) {
  data class Result(
    val backlitImage: File,
    val ambientImage: File
  )

  suspend fun grabImages(): Result {
    backlight.switch(on = true)
    val backlitFile = camera.grab(imageDir, "backlit")
    backlight.switch(on = false)

    ambientLight.switch(on = true)
    val ambientFile = camera.grab(imageDir, "ambient")
    ambientLight.switch(on = false)

    return Result(
      backlitImage = backlitFile,
      ambientImage = ambientFile
    )
  }
}
