package doh.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.db.createFilesystemDb
import doh.grab.ImageGrabber
import doh.grab.PiImageGrabber
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
  }

  @Binds
  abstract fun imageGrabber(piImageGrabber: PiImageGrabber): ImageGrabber
}
