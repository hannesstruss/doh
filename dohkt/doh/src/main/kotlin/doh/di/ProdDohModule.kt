package doh.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.db.createFilesystemDb
import doh.grab.Ambient
import doh.grab.Backlight
import doh.grab.Camera
import doh.grab.GpioLight
import doh.grab.GpioPin
import doh.grab.Light
import doh.grab.PiCamera
import java.io.File
import javax.inject.Singleton

@Module
abstract class ProdDohModule {
  companion object {
    @Provides
    @Singleton
    @ImageDir
    fun imageDir(): File {
      val home = System.getProperty("user.home")
      val dir = File(home, "/doh-images")

      if (!dir.exists()) {
        dir.mkdirs()
      }

      return dir
    }

    @Provides
    @Singleton
    fun dohDatabase(): DohDatabase {
      val home = System.getProperty("user.home")
      val file = File(home, "doh.sqlite")
      return createFilesystemDb(file)
    }

    @Provides
    @Singleton
    @Backlight
    fun backlight(): Light {
      return GpioLight(GpioPin(1))
    }

    @Provides
    @Singleton
    @Ambient
    fun ambientLight(): Light {
      return GpioLight(GpioPin(15))
    }
  }

  @Binds
  abstract fun camera(piCamera: PiCamera): Camera
}
