import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.ObjectFit
import kotlinx.css.backgroundColor
import kotlinx.css.objectFit
import kotlinx.css.properties.add
import kotlinx.css.properties.s
import kotlinx.css.properties.transform
import kotlinx.css.properties.transition
import kotlinx.css.transition
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.ReactElement
import react.setState
import styled.css
import styled.styledImg
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

external interface ImageStageProps : RProps {
  var src: String?
  var zoomLevel: ZoomLevel
}

external interface ImageStageState : RState {
  var dataUri: String?
}

enum class ZoomLevel(
  val cssScale: Double,
  val cssVerticalTranslation: Int
) {
  Normal(1.0, 0),
  Zoomed(2.5, -10),
  Intense(3.5, -12);

  val next: ZoomLevel
    get() = when (this) {
      Normal -> Zoomed
      Zoomed -> Intense
      Intense -> Normal
    }
}

class ImageStage : RComponent<ImageStageProps, ImageStageState>() {
  private val canvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement

  private suspend fun generateDataUri(src: String): String = suspendCoroutine { cont ->
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    val img = document.createElement("img") as HTMLImageElement
    img.crossOrigin = "anonymous"
    img.onload = {
      println("I loaded the image! ${img.width}x${img.height}")
      canvas.width = img.width
      canvas.height = img.height
      ctx.drawImage(img, 0.0, 0.0)
      ctx.beginPath()
      ctx.moveTo(0.0, 0.0)
      ctx.lineTo(img.width.toDouble(), img.height.toDouble())
      ctx.closePath()
      ctx.stroke()
      cont.resume(canvas.toDataURL())
    }
    img.src = src
  }

  private fun loadImage() {
    props.src?.let { src ->
      // TODO use better scope and cancel job
      GlobalScope.launch {
        val nextDataUri = generateDataUri(src)
        setState {
          dataUri = nextDataUri
        }
      }
    }
  }

  override fun componentDidMount() {
    loadImage()
  }

  override fun componentDidUpdate(prevProps: ImageStageProps, prevState: ImageStageState, snapshot: Any) {
    if (prevProps.src != props.src) {
      loadImage()
    }
  }

  override fun RBuilder.render() {
    styledImg(src = state.dataUri) {
      css {
        backgroundColor = Color.teal
        objectFit = ObjectFit.contain

        transition(
          property = "transform",
          duration = 0.25.s
        )

        transform {
          add("scale", props.zoomLevel.cssScale)
          add("translate", 0, "${props.zoomLevel.cssVerticalTranslation}%")
        }
      }
    }
  }
}

fun RBuilder.imageStage(handler: ImageStageProps.() -> Unit): ReactElement {
  return child(ImageStage::class) {
    this.attrs(handler)
  }
}
