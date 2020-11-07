package doh.di

import dagger.BindsInstance
import dagger.Component
import doh.DohApp
import doh.config.Config
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    DohModule::class,
    ProdDohModule::class
  ]
)
interface ProdDohComponent {
  companion object {
    operator fun invoke(config: Config): ProdDohComponent =
      DaggerProdDohComponent.factory().create(config)
  }

  @Component.Factory
  interface Factory {
    fun create(
      @BindsInstance config: Config
    ): ProdDohComponent
  }

  fun dohApp(): DohApp
}
