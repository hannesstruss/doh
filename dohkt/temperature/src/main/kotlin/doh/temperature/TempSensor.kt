package doh.temperature

interface TempSensor {
  /** Returns current temperature in degrees Celsius */
  suspend fun measure(): Double
}
