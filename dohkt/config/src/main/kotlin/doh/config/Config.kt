package doh.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
  val analyzerScriptCommand: String
)
