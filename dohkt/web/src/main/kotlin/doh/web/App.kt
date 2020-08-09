package doh.web

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun createApp() = embeddedServer(Netty, 8080) {
  routing {
    get("/") {
      call.respondText("Didel didel")
    }
  }
}
