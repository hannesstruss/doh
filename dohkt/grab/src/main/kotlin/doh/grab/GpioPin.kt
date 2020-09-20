package doh.grab

import kotlinx.coroutines.runBlocking

// We use the gpio cmd as it has the sticky bit and can be run without being root.
class GpioPin(private val id: Int) {
  init {
    runBlocking {
      "gpio export $id out".runCmd()
      "gpio mode $id out".runCmd()
    }
  }

  fun write(high: Boolean) {
    val value = if (high) "1" else "0"
    runBlocking {
      "gpio write $id $value".runCmd()
    }
  }
}
