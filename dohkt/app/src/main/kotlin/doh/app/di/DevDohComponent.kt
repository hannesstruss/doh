package doh.app.di

import dagger.Component
import doh.app.DohApp
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
    fun create(): DevDohComponent
  }
}
