package doh.app

import doh.db.DoughStatusRepo
import doh.web.createApp

fun main() {
  val repo = DoughStatusRepo(null)
  createApp(repo).start(wait = true)
}
