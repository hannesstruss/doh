package doh.meta

object Versions {
  const val ktor = "1.3.2"

  object KotlinX {
    const val coroutinesCore = "1.3.8"
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

  const val SqlDelightDriver = "com.squareup.sqldelight:sqlite-driver:1.4.0"
}
