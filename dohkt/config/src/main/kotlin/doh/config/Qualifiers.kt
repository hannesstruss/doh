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
annotation class AnalyzerHost

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class JWTSecret

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AdminPassword
