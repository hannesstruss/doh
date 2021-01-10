package doh.temperature

import doh.db.TemperatureReadingRepo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class TemperatureReaderLoop
@Inject constructor(
  private val tempSensor: TempSensor,
  private val temperatureReadingRepo: TemperatureReadingRepo
) {
  companion object {
    private val Frequency = Duration.ofMinutes(1)
  }

  suspend fun run() = coroutineScope {
    while (isActive) {
      val duration = measureTimeMillis {
        val temperature = tempSensor.measure()
        println("Measured temperature: ${"%.2f".format(temperature)}ºC")
        temperatureReadingRepo.insert(temperature)
      }

      delay(Frequency.toMillis() - duration)
    }
  }
}
