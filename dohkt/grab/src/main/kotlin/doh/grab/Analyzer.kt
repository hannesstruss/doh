package doh.grab

import doh.config.AnalyzerHost
import doh.shared.AnalyzerResult
import io.ktor.client.HttpClient
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.streams.asInput
import java.io.File
import javax.inject.Inject

class Analyzer
@Inject constructor(
  @AnalyzerHost private val analyzerHost: String
) {
  private val httpClient = HttpClient() {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }
  private val url = "$analyzerHost/analyze-images"

  suspend fun analyze(backlitFile: File, ambientFile: File): AnalyzerResult {
    require(backlitFile.exists()) { "Backlit file doesn't exist" }
    require(ambientFile.exists()) { "Ambient file doesn't exist" }

    val response: AnalyzerResponse = try {
      post(backlitFile, ambientFile)
    } catch (e: ServerResponseException) {
      throw RuntimeException("Calling analyzer service for images backlit=${backlitFile.name} ambient=${ambientFile.name} failed", e)
    }
    println(response)
    return AnalyzerResponse.toResult(response)
  }

  private suspend fun post(backlitFile: File, ambientFile: File): AnalyzerResponse {
    return httpClient.post(url) {
      body = MultiPartFormDataContent(
        formData {
          append(
            key = "backlit",
            value = InputProvider { backlitFile.inputStream().asInput() },
            headers = Headers.build {
              append(HttpHeaders.ContentType, "image/jpeg")
              append(HttpHeaders.ContentDisposition, "filename=backlit.jpg")
            }
          )

          append(
            key = "ambient",
            value = InputProvider { ambientFile.inputStream().asInput() },
            headers = Headers.build {
              append(HttpHeaders.ContentType, "image/jpeg")
              append(HttpHeaders.ContentDisposition, "filename=ambient.jpg")
            }
          )
        }
      )
    }
  }
}
