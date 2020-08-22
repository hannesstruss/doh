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
  }

  const val Slf4jSimple = "org.slf4j:slf4j-simple:1.7.30"

  object Ktor {
    const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    const val htmlBuilder = "io.ktor:ktor-html-builder:${Versions.ktor}"
  }

  object Dagger {
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
  }

  const val SqlDelightDriver = "com.squareup.sqldelight:sqlite-driver:1.4.1"
}
