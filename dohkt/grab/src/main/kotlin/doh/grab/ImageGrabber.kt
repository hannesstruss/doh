package doh.grab

import java.io.File

interface ImageGrabber {
  suspend fun grabImage(): File
}
