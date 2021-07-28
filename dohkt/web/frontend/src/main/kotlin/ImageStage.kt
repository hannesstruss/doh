import doh.shared.AnalyzerResult
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.ObjectFit
import kotlinx.css.objectFit
import kotlinx.css.properties.add
import kotlinx.css.properties.s
import kotlinx.css.properties.transform
import kotlinx.css.properties.transition
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLImageElement
import react.RBuilder
import react.RComponent
import react.RProps
import react.State
import react.setState
import styled.css
import styled.styledImg
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

external interface ImageStageProps : RProps {
  var src: String?
  var zoomLevel: ZoomLevel
  var doughData: AnalyzerResult.GlassPresent?
  var showAnalyzerOverlay: Boolean
}

external interface ImageStageState : State {
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

  private suspend fun generateDataUri(
    imageUrl: String,
    doughData: AnalyzerResult.GlassPresent?,
    drawAnalyzerOverlay: Boolean,
  ): String = suspendCoroutine { cont ->
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    val img = document.createElement("img") as HTMLImageElement
    img.crossOrigin = "anonymous"
    img.onload = {
      canvas.width = img.width
      canvas.height = img.height
      ctx.drawImage(img, 0.0, 0.0)

      if (drawAnalyzerOverlay) {
        ctx.lineWidth = 3.0

        if (doughData != null) {
          ctx.drawLevel("green", doughData.doughLevelY.toDouble())
          ctx.drawLevel("blue", doughData.rubberBandY.toDouble())
          ctx.drawLevel("red", doughData.glassBottomY.toDouble())
        }
      }

      cont.resume(canvas.toDataURL())
    }
    img.src = imageUrl
  }

  private fun CanvasRenderingContext2D.drawLevel(color: String, y: Double) {
    strokeStyle = color
    beginPath()
    moveTo(0.0, y)
    lineTo(canvas.width.toDouble(), y)
    closePath()
    stroke()
  }

  private fun loadImage() {
    props.src?.let { src ->
      // TODO use better scope and cancel job
      GlobalScope.launch {
        val nextDataUri = generateDataUri(src, props.doughData, props.showAnalyzerOverlay)
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
    if (prevProps.src != props.src || prevProps.showAnalyzerOverlay != props.showAnalyzerOverlay) {
      loadImage()
    }
  }

  override fun RBuilder.render() {
    styledImg(src = state.dataUri) {
      css {
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

fun RBuilder.imageStage(handler: ImageStageProps.() -> Unit) = child(ImageStage::class) {
  attrs {
    handler()
  }
}

