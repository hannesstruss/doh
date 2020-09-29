import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h1
import react.dom.h2
import react.setState
import styled.styledDiv

external interface AppState: RState {
  var selectedThing: String
}

class App : RComponent<RProps, AppState>() {
  override fun RBuilder.render() {
    styledDiv {
      h1 {
        +"People I love:"
      }

      h2 {
        +state.selectedThing
      }

      myList {
        things = listOf("Schnutsli", "Larsi", "Schnuppi")
        selectedThing = state.selectedThing
        onSelectThing = {
          setState {
            selectedThing = it
          }
        }
      }
    }
  }
}
