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
import kotlinx.html.h1
import kotlinx.html.img
import kotlinx.html.p
import java.io.File

fun createWebApp(
  repo: DoughStatusRepo,
  imagesDir: File
) = embeddedServer(Netty, 8080) {
  routing {
    get("/") {
      val status = repo.getLatestStatus()

      call.respondHtml {
        body {
          h1 {
            +"Hello!"
          }

          if (status != null) {
            p { +"Recorded at: ${status.recordedAt}" }
            p { +"Growth: ${status.growth}" }
            p {
              img(src = "/dough-images/${status.imageFileName}") {
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
