import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h1

external interface MyListProps : RProps {
  var things: List<String>
  var selectedThing: String
  var onSelectThing: (String) -> Unit
}

fun RBuilder.myList(handler: MyListProps.() -> Unit) =
  child(MyList::class) {
    this.attrs(handler)
  }

class MyList : RComponent<MyListProps, RState>() {
  override fun RBuilder.render() {
    for (thing in props.things) {
      h1 {
        key = thing
        attrs {
          onClickFunction = {
            props.onSelectThing(thing)
          }
        }
        if (thing == props.selectedThing) {
          +"❤️"
        }
        +thing
      }
    }
  }
}
