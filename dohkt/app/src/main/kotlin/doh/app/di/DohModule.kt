package doh.app.di

import dagger.Module
import dagger.Provides
import doh.grab.FakeImageGrabber
import doh.grab.ImageGrabber
import java.io.File

@Module
abstract class DohModule {
  companion object {
    @Provides
    fun imageGrabber(@ImageDir imageDir: File): ImageGrabber =
      FakeImageGrabber(imageDir)
  }
}
