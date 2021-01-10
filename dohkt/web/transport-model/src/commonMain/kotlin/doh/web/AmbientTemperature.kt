package doh.web

import kotlinx.serialization.Serializable

@Serializable
data class AmbientTemperature(val centigrades: Double?)
