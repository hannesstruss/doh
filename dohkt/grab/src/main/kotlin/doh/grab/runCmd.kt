package doh.grab

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Suppress("BlockingMethodInNonBlockingContext")
internal suspend fun String.runCmd() = coroutineScope {
  val cmd: String = this@runCmd

  val handle = (Math.random() * 10000).roundToInt()
//  println("Running [$handle] '$cmd' as UID ${System.getProperty("user.name")}")

  val process = Runtime.getRuntime().exec(cmd.split(" ").toTypedArray())
  val stdout = launch(IO) {
    process.inputStream.bufferedReader().use {
      while (true) {
        val line = it.readLine() ?: break
        println("[$handle]: $line")
      }
    }
  }
  val stderr = launch(IO) {
    process.errorStream.bufferedReader().use {
      while (true) {
        val line = it.readLine() ?: break
        println("[$handle] err: $line")
      }
    }
  }
  val resultCode = process.waitFor()
//  println("[$handle] exited with $resultCode")
  stdout.join()
  stderr.join()

  if (resultCode != 0) {
    throw RuntimeException("[$handle] '$cmd' exited with $resultCode")
  }
}
