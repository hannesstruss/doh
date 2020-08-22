package doh.app.di

import dagger.BindsInstance
import dagger.Component
import doh.app.DohApp
import java.io.File
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    DohModule::class,
    DevDohModule::class
  ]
)
interface DevDohComponent {
  fun dohApp(): DohApp

  @Component.Factory
  interface Factory {
    fun create(
      @BindsInstance @ImageDir imageDir: File
    ): DevDohComponent
  }
}
