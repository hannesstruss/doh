package doh.grab

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Backlight

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Ambient

interface Light {
  fun switch(on: Boolean)
}
