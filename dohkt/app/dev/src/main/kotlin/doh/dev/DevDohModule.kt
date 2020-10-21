package doh.dev

import dagger.Binds
import dagger.Module
import dagger.Provides
import doh.config.AnalyzerScriptCommand
import doh.config.Config
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.grab.Ambient
import doh.grab.Backlight
import doh.grab.Camera
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
    fun dohDatabase(): DohDatabase = createFakeDb()

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
    @AnalyzerScriptCommand
    fun analyzerScriptCommand(config: Config): String = config.analyzerScriptCommand
  }

  @Binds abstract fun camera(fakeCamera: FakeCamera): Camera
}
