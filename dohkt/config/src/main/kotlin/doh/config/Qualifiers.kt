package doh.config

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DatabasePath

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ImageDir

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AnalyzerScriptCommand
