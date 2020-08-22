package doh.app.di

import dagger.Module
import dagger.Provides
import doh.db.DohDatabase
import doh.db.createInMemoryDb
import doh.grab.FakeImageGrabber
import doh.grab.ImageGrabber
import java.io.File
import javax.inject.Singleton

@Module
abstract class DevDohModule {
  companion object {
    @Provides
    fun imageGrabber(@ImageDir imageDir: File): ImageGrabber =
      FakeImageGrabber(imageDir)

    @Provides
    @Singleton
    fun dohDatabase(): DohDatabase = createInMemoryDb()
  }
}
