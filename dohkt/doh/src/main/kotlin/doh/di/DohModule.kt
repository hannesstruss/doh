package doh.di

import dagger.Module
import dagger.Provides
import doh.db.DohDatabase
import doh.db.DoughStatusRepo

@Module
abstract class DohModule {
  companion object {
    @Provides
    fun dohStatusRepo(db: DohDatabase): DoughStatusRepo = DoughStatusRepo(db)
  }
}
