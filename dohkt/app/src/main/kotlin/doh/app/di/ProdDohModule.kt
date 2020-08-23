package doh.app.di

import dagger.Module
import dagger.Provides
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.grab.ImageGrabber
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
      val dir = File(home + "/doh-images")

      if (!dir.exists()) {
        dir.mkdirs()
      }

      return dir
    }

    @Provides
    fun imageGrabber(): ImageGrabber = TODO()

    @Provides
    fun dohDatabase(): DohDatabase = TODO()
  }
}
