package doh.temperature

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ParseW1SensorFileTest {
  private val SensorFileContent = """
    |b2 01 4b 46 7f ff 0e 10 8c : crc=8c YES
    |b2 01 4b 46 7f ff 0e 10 8c t=27125
  """.trimMargin("|")

  @Test
  fun `should parse sensor file`() {
    assertEquals(27.125, parseW1SensorFile(SensorFileContent))
  }
}
