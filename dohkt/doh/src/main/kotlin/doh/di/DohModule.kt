package doh.di

import dagger.Module
import dagger.Provides
import doh.config.AnalyzerHost
import doh.config.ImageDir
import doh.db.DohDatabase
import doh.db.DoughAnalysisRepo
import doh.db.DoughStatusRepo
import doh.db.createFilesystemDb
import java.io.File
import javax.inject.Singleton

@Module
abstract class DohModule {
  companion object {
    private const val ImageDirEnvVar = "DOH_IMAGEDIR"
    private const val DatabasePathEnvVar = "DOH_DATABASEPATH"
    private const val AnalyzerHostEnvVar = "DOH_ANALYZERHOST"

    @Provides
    @Singleton
    @ImageDir
    fun imageDir(): File {
      val pathname = checkNotNull(System.getenv(ImageDirEnvVar)) { "$ImageDirEnvVar not set" }
      val dir = File(pathname)

      if (!dir.exists()) {
        dir.mkdirs()
      }

      return dir
    }

    @Provides
    @Singleton
    @AnalyzerHost
    fun analyzerHost(): String {
      return checkNotNull(System.getenv(AnalyzerHostEnvVar)) { "$AnalyzerHostEnvVar not set" }
    }

    @Provides
    @Singleton
    fun dohDatabase(): DohDatabase {
      val path = checkNotNull(System.getenv(DatabasePathEnvVar)) { "$DatabasePathEnvVar not set" }
      val file = File(path)
      return createFilesystemDb(file)
    }

    @Provides
    fun dohStatusRepo(db: DohDatabase): DoughStatusRepo = DoughStatusRepo(db)

    @Provides
    fun doughAnalysisRepo(db: DohDatabase): DoughAnalysisRepo = DoughAnalysisRepo(db)
  }
}
