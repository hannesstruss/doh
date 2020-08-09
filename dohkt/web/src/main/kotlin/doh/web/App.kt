package doh.web

import doh.db.DoughStatusRepo
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun createApp(repo: DoughStatusRepo) = embeddedServer(Netty, 8080) {
  routing {
    get("/") {
      call.respondText("Didel didel: " + repo.getAll().map { it.growth }.joinToString(", "))
    }
  }
}
