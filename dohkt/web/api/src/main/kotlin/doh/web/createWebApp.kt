package doh.web

import doh.db.DoughStatusRepo
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CachingHeaders
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
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
import kotlinx.html.ScriptType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.unsafe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit

private const val ImagesPath = "/dough-images"

fun createWebApp(
  repo: DoughStatusRepo,
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
        val latestStatuses = repo.getAllAfter(Instant.now().minus(12, ChronoUnit.HOURS))
          .map { DoughStatusViewModel.fromDoughStatus(ImagesPath, it) }

        call.respond(latestStatuses)
      }

      get("/old") {
        val latestStatuses = repo.getAllAfter(Instant.now().minus(12, ChronoUnit.HOURS))
          .map { DoughStatusViewModel.fromDoughStatus(ImagesPath, it) }

        call.respondHtml {
          head {
            meta {
              name = "viewport"
              content = "width=device-width, initial-scale=1"
            }

            script(type = "application/json") {
              attributes["id"] = "dough-status-json"
              unsafe {
                val json = Json.encodeToString(latestStatuses)
                +json
              }
            }

            style {
              unsafe {
                raw("""
                button {
                  padding: 1em;
                  width: 4em;
                  height: 4em;
                }
                
                img#dough-img {
                  width: 600px;
                  max-width: 100%;
                }
              """.trimIndent())
              }
            }
          }
          body {
            h1 {
              +"Hello!"
            }

            p {
              +"Recorded at: "
              span {
                attributes["id"] = "recorded-at"
              }
            }
            div {
              attributes["id"] = "time-buttons"
              button {
                +"-30"
              }
              button {
                +"-10"
              }
              button {
                +"X"
              }
              button {
                +"+10"
              }
              button {
                +"+30"
              }
            }
            p {
              img {
                attributes["id"] = "dough-img"
              }
            }

            script(type = ScriptType.textJavaScript) {
              unsafe {
                raw(
                  """
                const json = document.querySelector("#dough-status-json").innerText;
                const doughStatuses = JSON.parse(json);
                
                console.log(doughStatuses.map(ds => ds.recordedAt));
                
                var currentStatusIndex = -1;
                
                function showStatus(index) {
                  currentStatusIndex = index;
                  console.log(doughStatuses[currentStatusIndex].recordedAt);
                  const status = doughStatuses[index];
                  document.querySelector("#recorded-at").innerText = status.recordedAt;
                  document.querySelector("#dough-img").src = status.backlitImagePath;
                }
                
                // Need more sleep
                function findStatusIndex(timestampDelta) {
                  const currentTimestamp = doughStatuses[currentStatusIndex].recordedAtEpochSeconds;
                  const targetTimestamp = currentTimestamp + timestampDelta;
                  
                  const labeledStatuses = doughStatuses.map((status, index) => [
                    status, index, Math.abs(targetTimestamp - status.recordedAtEpochSeconds)
                  ]);
                  
                  const sorted = labeledStatuses.sort((lhs, rhs) => lhs[2] - rhs[2]);
                  
                  console.log(sorted);
                  
                  return sorted[0][1];
                }
                
                if (doughStatuses) {
                  showStatus(doughStatuses.length - 1);
                }
                
                const buttons = document.querySelector("#time-buttons").children;
                const intervals = [-30, -10, Number.MAX_SAFE_INTEGER, 10, 30];
                for (let n = 0; n < intervals.length; n++) {
                  const button = buttons[n];
                  button.addEventListener("click", () => {
                    const newInterval = intervals[n];
                    showStatus(findStatusIndex(newInterval * 60)); 
                  });
                }
              """.trimIndent()
                )
              }
            }
          }
        }
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

      static("/") {
        resources("doh.frontend")
      }
    }
  }
}
