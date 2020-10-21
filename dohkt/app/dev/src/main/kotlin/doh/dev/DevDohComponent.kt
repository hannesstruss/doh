package doh.dev

import dagger.BindsInstance
import dagger.Component
import doh.DohApp
import doh.config.Config
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
    operator fun invoke(config: Config) = DaggerDevDohComponent.factory().create(config)
  }

  fun dohApp(): DohApp

  @Component.Factory
  interface Factory {
    fun create(
      @BindsInstance config: Config
    ): DevDohComponent
  }
}
