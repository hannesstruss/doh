package doh.grab

class GpioLight(private val pin: GpioPin) : Light {
  override fun switch(on: Boolean) {
    pin.write(on)
  }
}
