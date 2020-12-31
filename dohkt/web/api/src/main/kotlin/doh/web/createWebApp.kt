package doh.web

import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.shared.AnalyzerResult
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

private const val ImagesPath = "/dough-images"

fun createWebApp(
  doughStatusRepo: DoughStatusRepo,
  doughAnalysisRepo: DoughAnalysisRepo,
  imagesDir: File
): NettyApplicationEngine {
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

    install(CallLogging)

    routing {
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
        val latest = doughStatusRepo.getLatestStatus()
        val last = latest?.recordedAt.toString()
        call.respondText("This will work eventually. Last status from $last")
      }

      static("/") {
        resources("doh.frontend")
      }
    }
  }
}
