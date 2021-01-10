package doh.temperature

private val Pattern = """t=(\d+)""".toRegex()

fun parseW1SensorFile(fileContents: String): Double {
  val result = checkNotNull(Pattern.find(fileContents))
  return result.groupValues.get(1).toDouble() / 1000
}
