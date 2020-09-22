package doh.di

import dagger.Component
import doh.DohApp
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
    operator fun invoke() = DaggerProdDohComponent.create()
  }
  fun dohApp(): DohApp
}
