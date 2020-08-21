package doh.app.di

import dagger.Module
import dagger.Provides
import doh.db.DohDatabase
import doh.db.DoughStatusRepo
import doh.db.createInMemoryDb
import doh.grab.FakeImageGrabber
import doh.grab.ImageGrabber
import java.io.File
import javax.inject.Singleton

@Module
abstract class DohModule {
  companion object {
    @Provides
    fun imageGrabber(@ImageDir imageDir: File): ImageGrabber =
      FakeImageGrabber(imageDir)

    @Provides
    @Singleton
    fun dohDatabase(): DohDatabase = createInMemoryDb()

    @Provides
    fun dohStatusRepo(db: DohDatabase): DoughStatusRepo = DoughStatusRepo(db)
  }
}
