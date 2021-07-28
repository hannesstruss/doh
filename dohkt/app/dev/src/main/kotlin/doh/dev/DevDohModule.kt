package doh.dev

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.AdminPassword
import doh.config.JWTSecret
import doh.grab.Ambient
import doh.grab.Backlight
import doh.grab.Camera
import doh.grab.Light
import doh.temperature.FakeTempSensor
import doh.temperature.TempSensor
import javax.inject.Singleton

@Module
abstract class DevDohModule {
  companion object {
    @Provides
    @Singleton
    @Backlight
    fun backlight(): Light {
      return FakeLight("Backlight")
    }

    @Provides
    @Singleton
    @Ambient
    fun ambientLight(): Light {
      return FakeLight("Ambient")
    }

    @Provides
    @JWTSecret
    fun jwtSecret(): String = "hunter2"

    @Provides
    @AdminPassword
    fun adminPassword(): String = "hunter2"
  }

  @Binds
  abstract fun camera(fakeCamera: FakeCamera): Camera

  @Binds
  abstract fun tempSensor(fakeTempSensor: FakeTempSensor): TempSensor
}
