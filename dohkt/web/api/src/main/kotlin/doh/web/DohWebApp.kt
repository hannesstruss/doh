package doh.web

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import doh.config.AdminPassword
import doh.config.ImageDir
import doh.config.JWTSecret
import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.db.TemperatureReadingRepo
import doh.shared.AnalyzerResult
import doh.shared.growth
import doh.temperature.TempSensor
import doh.web.helpers.formattedDuration
import doh.web.helpers.metrics
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.auth.principal
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
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
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
  @ImageDir private val imagesDir: File,
  @JWTSecret private val jwtSecret: String,
  @AdminPassword private val adminPassword: String
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
        host("dough.ai", schemes = listOf("http", "https"))
      }

      install(Locations)
      install(CallLogging)
      install(Authentication) {
        jwt("auth-jwt") {
          realm = "doh"
          verifier(
            JWT
              .require(Algorithm.HMAC256(jwtSecret))
              .withAudience("https://dough.ai/")
              .withIssuer("https://dough.ai/")
              .build()
          )
          validate { credential ->
            if (credential.payload.getClaim("username").asString() != "") {
              JWTPrincipal(credential.payload)
            } else {
              null
            }
          }
        }
      }

      routing {
        @Location("/doughstatuses/{id}")
        data class DoughStatusDetail(val id: String)

        post("/login") {
          val (username, password) = call.receive<Credentials>()
          if (username == "admin" && password == adminPassword) {
            val token = JWT.create()
              .withAudience("https://dough.ai/")
              .withIssuer("https://dough.ai/")
              .withClaim("username", username)
              .withExpiresAt(Date(Long.MAX_VALUE))
              .sign(Algorithm.HMAC256(jwtSecret))

            call.respond(hashMapOf("token" to token))
          } else {
            call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)
          }
        }

        authenticate("auth-jwt") {
          get("/secret") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
          }
        }

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
              analyzerResults[it.id] as? AnalyzerResult.GlassPresent
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
