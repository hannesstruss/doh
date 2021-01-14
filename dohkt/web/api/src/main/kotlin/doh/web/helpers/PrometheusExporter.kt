
package doh.web.helpers

import doh.web.helpers.MetricType.gauge
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Suppress("EnumEntryName")
private enum class MetricType {
  gauge
}

private class Metric(
  val type: MetricType,
  val name: String,
  val help: String?,
  val fetch: suspend () -> Double?
)

interface MetricsDefinition {
  fun gauge(name: String, help: String? = null, fetch: suspend () -> Double?)
}

private class PrometheusExporter : MetricsDefinition {
  private val metrics = mutableListOf<Metric>()

  override fun gauge(name: String, help: String?, fetch: suspend () -> Double?) {
    metrics += Metric(type = gauge, name = name, help = help, fetch = fetch)
  }

  suspend fun export(): String = coroutineScope {
    val sb = StringBuilder()

    val deferreds = metrics.map { metric ->
      async { metric.fetch() }
    }

    metrics.zip(deferreds.awaitAll()).forEach { (metric, value) ->
      if (value != null) {
        if (!metric.help.isNullOrBlank()) {
          sb.appendLine("# HELP ${metric.name} ${metric.help}")
        }
        sb.appendLine("# TYPE ${metric.name} ${metric.type.name}")
        sb.appendLine("${metric.name} $value")
      }
    }

    sb.toString()
  }
}

suspend fun metrics(block: MetricsDefinition.() -> Unit): String {
  val exporter = PrometheusExporter()
  exporter.block()
  return exporter.export()
}
