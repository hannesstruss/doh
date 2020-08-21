package doh.app.di

import dagger.BindsInstance
import dagger.Component
import doh.app.DohApp
import java.io.File

@Component(
  modules = [DohModule::class]
)
interface DohComponent {
  fun dohApp(): DohApp

  @Component.Factory
  interface Factory {
    fun create(
      @BindsInstance @ImageDir imageDir: File
    ): DohComponent
  }
}