package doh.app.di

import dagger.Component
import doh.app.DohApp
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    DohModule::class,
    ProdDohModule::class
  ]
)
interface ProdDohComponent {
  fun dohApp(): DohApp
}
