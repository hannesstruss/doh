package doh.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.grab.Ambient
import doh.grab.Backlight
import doh.grab.Camera
import doh.grab.GpioLight
import doh.grab.GpioPin
import doh.grab.Light
import doh.grab.PiCamera
import doh.temperature.PiTempSensor
import doh.temperature.TempSensor
import javax.inject.Singleton

@Module
abstract class PiDohModule {
  companion object {
    @Provides
    @Singleton
    @Backlight
    fun backlight(): Light {
      return GpioLight(GpioPin(14))
    }

    @Provides
    @Singleton
    @Ambient
    fun ambientLight(): Light {
      return GpioLight(GpioPin(18))
    }
  }

  @Binds
  abstract fun camera(piCamera: PiCamera): Camera

  @Binds
  abstract fun tempSensor(piTempSensor: PiTempSensor): TempSensor
}
