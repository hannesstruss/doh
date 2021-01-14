package doh.temperature

import javax.inject.Inject

class FakeTempSensor @Inject constructor() : TempSensor {
  override suspend fun measure(): Double {
    return 27.0 + Math.sin(System.currentTimeMillis().toDouble() / 10000) * 2
  }
}
