package doh.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.db.createInMemoryDb
import doh.grab.FakeImageGrabber
import doh.grab.ImageGrabber
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
  }

  @Binds abstract fun imageGrabber(fakeImageGrabber: FakeImageGrabber): ImageGrabber
}
