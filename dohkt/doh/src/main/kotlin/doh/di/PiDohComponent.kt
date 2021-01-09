package doh.di

import dagger.Component
import doh.DohApp
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    DohModule::class,
    PiDohModule::class
  ]
)
interface PiDohComponent {
  companion object {
    operator fun invoke() = DaggerPiDohComponent.create()
  }
  fun dohApp(): DohApp
}
