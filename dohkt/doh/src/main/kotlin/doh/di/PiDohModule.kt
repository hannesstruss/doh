package doh.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.AdminPassword
import doh.config.JWTSecret
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

    @Provides @JWTSecret
    fun jwtSecret(): String = System.getenv("DOH_JWT_SECRET")
      ?: throw IllegalStateException("DOH_JWT_SECRET is not defined")

    @Provides @AdminPassword
    fun adminPassword(): String = System.getenv("DOH_ADMIN_PASSWORD")
      ?: throw IllegalStateException("DOH_ADMIN_PASSWORD is not defined")
  }

  @Binds
  abstract fun camera(piCamera: PiCamera): Camera

  @Binds
  abstract fun tempSensor(piTempSensor: PiTempSensor): TempSensor
}
