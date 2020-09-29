package doh.meta

object Versions {
  const val ktor = "1.4.0"
  const val dagger = "2.28.3"

  object KotlinX {
    const val coroutinesCore = "1.3.9"
  }
}

object Deps {
  object KotlinX {
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KotlinX.coroutinesCore}"
    const val serializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC"
  }

  const val logback = "ch.qos.logback:logback-classic:1.2.3"

  object Ktor {
    const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    const val serialization = "io.ktor:ktor-serialization:${Versions.ktor}"
    const val htmlBuilder = "io.ktor:ktor-html-builder:${Versions.ktor}"
  }

  object Dagger {
    const val inject = "javax.inject:javax.inject:1"
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
  }

  const val SqlDelightDriver = "com.squareup.sqldelight:sqlite-driver:1.4.1"
}
