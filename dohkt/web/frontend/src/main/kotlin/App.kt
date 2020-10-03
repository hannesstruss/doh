import doh.web.DoughStatusViewModel
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.ObjectFit
import kotlinx.css.Overflow
import kotlinx.css.TextAlign
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.objectFit
import kotlinx.css.overflowY
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.properties.add
import kotlinx.css.properties.transform
import kotlinx.css.textAlign
import kotlinx.css.vh
import kotlinx.css.width
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledButton
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
}

val AppState.selectedStatus: DoughStatusViewModel?
  get() = selectedIndex.takeIf { it >= 0 }?.let { doughStatuses.elementAt(it) }

class App : RComponent<RProps, AppState>() {
  override fun AppState.init() {
    doughStatuses = emptyList()
    selectedIndex = -1
    zoomedIn = false
    showAmbient = false
  }

  override fun componentDidMount() {
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
        height = 100.vh
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
            textAlign = TextAlign.center
            padding = "1em"
          }

          styledButton {
            navButtonStyles()
            attrs {
              onClickFunction = {
                setState {
                  selectedIndex = maxOf(0, selectedIndex - 1)
                }
              }
            }
            +"<"
          }
          styledButton {
            navButtonStyles()
            attrs {
              onClickFunction = {
                setState {
                  zoomedIn = !zoomedIn
                }
              }
            }
            if (state.zoomedIn) {
              +"-"
            } else {
              +"+"
            }
          }
          styledButton {
            navButtonStyles()
            attrs {
              onClickFunction = {
                setState {
                  showAmbient = !showAmbient
                }
              }
            }
            +"💡"
          }
          styledButton {
            navButtonStyles()
            attrs {
              onClickFunction = {
                setState {
                  selectedIndex = minOf(state.doughStatuses.lastIndex, selectedIndex + 1)
                }
              }
            }
            +">"
          }
        }
      }
    }
  }
}
