package doh.web

import doh.db.DoughStatusRepo
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.meta
import kotlinx.html.p
import java.io.File
import java.time.ZoneId

fun createWebApp(
  repo: DoughStatusRepo,
  imagesDir: File
) = embeddedServer(Netty, 8080) {
  routing {
    get("/") {
      val status = repo.getLatestStatus()

      call.respondHtml {
        head {
          meta {
            name = "viewport"
            content = "width=device-width, initial-scale=1"
          }
        }
        body {
          h1 {
            +"Hello!"
          }

          if (status != null) {
            p { +"Recorded at: ${status.recordedAt.atZone(ZoneId.of("Europe/Berlin"))}" }
            p { +"Growth: ${status.growth}" }
            p { +"Total records: ${repo.getAll().size}" }
            div {
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
              img(src = "/dough-images/${status.imageFile}") {
                width = "100%"
              }
            }
          }
        }
      }
    }

    static("dough-images") {
      files(imagesDir)
    }
  }
}
