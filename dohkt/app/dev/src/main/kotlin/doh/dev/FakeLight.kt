package doh.dev

import doh.grab.Light

class FakeLight(private val name: String) : Light {
  override fun switch(on: Boolean) {
    val status = if (on) "on" else "off"
    println("Light[$name]: $status")
  }
}
