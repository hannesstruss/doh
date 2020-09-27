package doh.grab

import java.io.File

interface Camera {
  /**
   * Grab an image from the camera and place it into [directory].
   * Use [classifier] as part of the filename.
   */
  suspend fun grab(directory: File, classifier: String): File
}
