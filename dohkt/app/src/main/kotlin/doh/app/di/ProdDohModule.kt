package doh.app.di

import dagger.Module
import dagger.Provides
import doh.db.DohDatabase
import doh.grab.ImageGrabber

@Module
abstract class ProdDohModule {
  companion object {
    @Provides
    fun imageGrabber(): ImageGrabber = TODO()

    @Provides
    fun dohDatabase(): DohDatabase = TODO()
  }
}
