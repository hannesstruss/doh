package doh.web

import doh.config.ImageDir
import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.db.TemperatureReadingRepo
import doh.db.mappers.toAnalyzerResult
import doh.shared.AnalyzerResult
import doh.shared.growth
import doh.temperature.TempSensor
import doh.web.helpers.formattedDuration
import doh.web.helpers.metrics
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.files
import io.ktor.http.content.resolveResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToInt

private const val ImagesPath = "/dough-images"

class DohWebApp
@Inject constructor(
  private val doughStatusRepo: DoughStatusRepo,
  private val doughAnalysisRepo: DoughAnalysisRepo,
  private val temperatureReadingRepo: TemperatureReadingRepo,
  private val tempSensor: TempSensor,
  @ImageDir private val imagesDir: File
) {
  fun createWebApp(): NettyApplicationEngine {
    return embeddedServer(Netty, 8080) {
      install(ContentNegotiation) {
        json()
      }

      install(CORS) {
        host("localhost:8081")
        host("doh.2e3.de:8080")
        host("doh.2e3.de:8081")
        host("192.168.1.28:8081")
      }

      install(Locations)
      install(CallLogging)

      routing {
        @Location("/doughstatuses/{id}")
        data class DoughStatusDetail(val id: String)

        get<DoughStatusDetail> { route ->
          val uuid = UUID.fromString(route.id)
          val status = doughStatusRepo.getById(uuid)
          if (status != null) {
            val viewModel = DoughStatusViewModel.fromDoughStatus(
              ImagesPath,
              status,
              doughAnalysisRepo.forDoughStatus(uuid)
            )
            call.respond(viewModel)
          } else {
            call.respondText(status = HttpStatusCode.NotFound) { "Not found" }
          }
        }

        get("/doughstatuses") {
          val latestStatuses = doughStatusRepo.getAllAfter(Instant.now().minus(12, ChronoUnit.HOURS))
          val analyzerResults = doughAnalysisRepo.forDoughStatuses(latestStatuses.map { it.id })

          val viewModels = latestStatuses.map {
            DoughStatusViewModel.fromDoughStatus(
              ImagesPath,
              it,
              analyzerResults.get(it.id) as? AnalyzerResult.GlassPresent
            )
          }

          call.respond(viewModels)
        }

        get("/ambient-temperature") {
          val latestTemp = temperatureReadingRepo.getLatest()
          call.respond(AmbientTemperature(latestTemp))
        }

        get("/metrics") {
          val response = metrics {
            gauge("ambient_temp", help = "Ambient temp from the sensor.") { tempSensor.measure() }

            gauge("dough_growth", help = "Growth of the dough in percent.") {
              doughStatusRepo.getLatestStatus()
                ?.let { doughAnalysisRepo.forDoughStatus(it.id) }
                ?.growth
                ?.let { it * 100.0 }
            }
          }
          call.respond(response)
        }

        static("dough-images") {
          files(imagesDir)
          resources("doh.dev")
        }

        get("/") {
          val html = call.resolveResource("index.html", "doh.frontend")
          if (html != null) {
            call.respond(html)
          } else {
            call.respondText("Not found", status = HttpStatusCode.NotFound)
          }
        }

        get("/siri") {
          val latestStatus = doughStatusRepo.getLatestStatus()
          val latestAnalysis = latestStatus?.id?.let { doughAnalysisRepo.forDoughStatus(it) }
          val growth = latestAnalysis?.growth

          if (latestStatus != null) {
            var response = "Status from ${latestStatus.recordedAt.formattedDuration(Instant.now())}."
            if (growth != null) {
              response += " Growth: ${(growth * 100).roundToInt()}%"
            }
            call.respondText(response)
          } else {
            call.respondText("No Status yet!")
          }
        }

        static("/") {
          resources("doh.frontend")
        }
      }
    }
  }
}
