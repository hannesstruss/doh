package doh.meta

object Versions {
  const val ktor = "1.4.2"
  const val dagger = "2.28.3"

  object KotlinX {
    const val coroutinesCore = "1.3.9"
    const val serialization = "1.0.1"
  }
}

object Deps {
  object KotlinX {
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KotlinX.coroutinesCore}"
    const val serializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.KotlinX.serialization}"
    const val serializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KotlinX.serialization}"
  }

  const val logback = "ch.qos.logback:logback-classic:1.2.3"

  object Ktor {
    const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    const val serialization = "io.ktor:ktor-serialization:${Versions.ktor}"
    const val htmlBuilder = "io.ktor:ktor-html-builder:${Versions.ktor}"
    const val clientCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val clientJsonJs = "io.ktor:ktor-client-json:${Versions.ktor}"
  }

  object Dagger {
    const val inject = "javax.inject:javax.inject:1"
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
  }

  const val SqlDelightDriver = "com.squareup.sqldelight:sqlite-driver:1.4.1"
}
