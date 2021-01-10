package doh.temperature

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class PiTempSensor @Inject constructor() : TempSensor {
  companion object {
    private val Source = File("/tempsensor/w1_slave")
  }

  override suspend fun measure(): Double = withContext(IO) {
    val content = PiTempSensor.Source.readText()
    parseW1SensorFile(content)
  }
}
