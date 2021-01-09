package doh.grab

import java.io.File

class GpioPin(id: Int) {

  private val exportPath = "/sys/class/gpio/export"
  private val directionPath = "/sys/class/gpio/gpio$id/direction"
  private val valuePath = "/sys/class/gpio/gpio$id/value"

  private val valueFile = File(valuePath)

  init {
    if (!valueFile.exists()) {
      File(exportPath).writeText(id.toString())
    }
    File(directionPath).writeText("out")
  }

  fun write(high: Boolean) {
    val value = if (high) "1" else "0"
    valueFile.writeText(value)
  }
}
