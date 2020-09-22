package doh.dev

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.db.createInMemoryDb
import doh.grab.Ambient
import doh.grab.Backlight
import doh.grab.FakeImageGrabber
import doh.grab.FakeLight
import doh.grab.ImageGrabber
import doh.grab.Light
import java.io.File
import java.nio.file.Files
import javax.inject.Singleton

@Module
abstract class DevDohModule {
  companion object {
    @Provides
    @Singleton
    @ImageDir
    fun imagesDir(): File = Files.createTempDirectory("doh-fakes").toFile()

    @Provides
    @Singleton
    fun dohDatabase(): DohDatabase = createInMemoryDb()

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
  }

  @Binds abstract fun imageGrabber(fakeImageGrabber: FakeImageGrabber): ImageGrabber
}
