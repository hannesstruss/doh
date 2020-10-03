import doh.web.DoughStatusViewModel
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.JustifyContent
import kotlinx.css.ObjectFit
import kotlinx.css.Overflow
import kotlinx.css.TextAlign
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexWrap
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.objectFit
import kotlinx.css.overflowY
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.properties.add
import kotlinx.css.properties.s
import kotlinx.css.properties.transform
import kotlinx.css.properties.transition
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.css.transition
import kotlinx.css.width
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledImg
import styled.styledP

private val BackendHost = "http://${window.location.hostname}:8080"

external interface AppState : RState {
  var doughStatuses: List<DoughStatusViewModel>
  var selectedIndex: Int
  var zoomedIn: Boolean
  var showAmbient: Boolean
  var viewportHeight: Int
}

val AppState.selectedStatus: DoughStatusViewModel?
  get() = selectedIndex.takeIf { it >= 0 }?.let { doughStatuses.elementAt(it) }

val AppState.isAtLastIndex: Boolean
  get() = selectedIndex == doughStatuses.lastIndex

val AppState.isAtFirstIndex: Boolean
  get() = selectedIndex == 0

class App : RComponent<RProps, AppState>() {
  override fun AppState.init() {
    doughStatuses = emptyList()
    selectedIndex = -1
    zoomedIn = false
    showAmbient = false
    viewportHeight = window.innerHeight
  }

  override fun componentDidMount() {
    window.onresize = {
      setState {
        viewportHeight = window.innerHeight
      }
    }

    GlobalScope.launch {
      val result: Array<DoughStatusViewModel> = window.fetch("$BackendHost/doughstatuses")
        .await()
        .json()
        .await()
        .unsafeCast<Array<DoughStatusViewModel>>()

      setState {
        doughStatuses = result.toList()
        selectedIndex = doughStatuses.lastIndex
      }
    }
  }

  override fun RBuilder.render() {
    styledDiv {
      css {
        display = Display.flex
        flexDirection = FlexDirection.column
        height = state.viewportHeight.px
      }

      styledH1 {
        css {
          textAlign = TextAlign.center
        }
        +"We have ${state.doughStatuses.count()} statuses."
      }
      styledP {
        css {
          textAlign = TextAlign.center
        }
        state.selectedStatus?.let {
          +it.recordedAt
        } ?: run {
          +"Loading"
        }
      }
      state.selectedStatus?.let {
        styledDiv {
          css {
            display = Display.flex
            justifyContent = JustifyContent.center
            height = 100.pct
            overflowY = Overflow.hidden
          }
          val imgPath = if (state.showAmbient && it.ambientImagePath != null) {
            it.ambientImagePath
          } else {
            it.backlitImagePath
          }
          styledImg(src = BackendHost + imgPath) {
            css {
              width = 100.pct
              objectFit = ObjectFit.contain
              transition(
                property = "transform",
                duration = 0.25.s
              )

              if (state.zoomedIn) {
                transform {
                  add("scale", 2.5)
                  add("translate", 0, "-10%")
                }
              }
            }
          }
        }
        styledDiv {
          css {
            display = Display.flex
            flexDirection = FlexDirection.row
            flexWrap = FlexWrap.wrap
            justifyContent = JustifyContent.spaceAround
            padding = "1em"
          }

          navButton("⏪") {
            setState {
              selectedIndex = maxOf(0, selectedIndex - 3)
            }
          }
          navButton(if (state.isAtFirstIndex) "✋" else "👈") {
            setState {
              selectedIndex = maxOf(0, selectedIndex - 1)
            }
          }
          navButton(if (state.zoomedIn) "🌍" else "🔍") {
            setState {
              zoomedIn = !zoomedIn
            }
          }
          navButton(if (state.showAmbient) "🌚" else "💡") {
            setState {
              showAmbient = !showAmbient
            }
          }
          navButton(if (state.isAtLastIndex) "✋" else "👉") {
            setState {
              selectedIndex = minOf(state.doughStatuses.lastIndex, selectedIndex + 1)
            }
          }
          navButton("⏩") {
            setState {
              selectedIndex = minOf(state.doughStatuses.lastIndex, selectedIndex + 3)
            }
          }
        }
      }
    }
  }
}
