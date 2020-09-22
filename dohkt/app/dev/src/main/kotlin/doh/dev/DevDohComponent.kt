package doh.dev

import dagger.Component
import doh.DohApp
import doh.di.DohModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    DohModule::class,
    DevDohModule::class
  ]
)
interface DevDohComponent {
  companion object {
    operator fun invoke() = DaggerDevDohComponent.create()
  }

  fun dohApp(): DohApp

  @Component.Factory
  interface Factory {
    fun create(): DevDohComponent
  }
}
