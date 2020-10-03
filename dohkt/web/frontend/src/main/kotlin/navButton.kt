import kotlinx.css.VerticalAlign
import kotlinx.css.em
import kotlinx.css.height
import kotlinx.css.padding
import kotlinx.css.verticalAlign
import kotlinx.css.width
import kotlinx.html.BUTTON
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import styled.StyledDOMBuilder
import styled.css
import styled.styledButton

fun StyledDOMBuilder<BUTTON>.navButtonStyles() {
  css {
    height = 3.em
    width = 3.em
    verticalAlign = VerticalAlign.top
  }
}

fun RBuilder.navButton(label: String, clickHandler: (Event) -> Unit) {
  styledButton {
    navButtonStyles()
    attrs {
      onClickFunction = clickHandler
    }
    +label
  }
}
