import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.TextAlign
import kotlinx.css.VerticalAlign
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.border
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.em
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.marginBottom
import kotlinx.css.marginLeft
import kotlinx.css.marginRight
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.rem
import kotlinx.css.textAlign
import kotlinx.css.verticalAlign
import kotlinx.css.width
import kotlinx.html.A
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.dom.span
import styled.StyledDOMBuilder
import styled.css
import styled.styledA
import styled.styledSpan

fun StyledDOMBuilder<A>.navButtonStyles() {
  css {
    height = 44.px
    width = 44.px
    verticalAlign = VerticalAlign.top
    cursor = Cursor.pointer
    marginBottom = 1.em
    display = Display.flex
    alignItems = Align.center
    justifyContent = JustifyContent.spaceBetween
    textAlign = TextAlign.center
    backgroundColor = Color.white
    fontSize = 1.3.rem
  }
}

fun RBuilder.navButton(label: String, clickHandler: (Event) -> Unit) {
  styledA {
    navButtonStyles()
    attrs {
      onClickFunction = clickHandler
    }
    styledSpan {
      css {
        width = 100.pct
        textAlign = TextAlign.center
      }
      +label
    }
  }
}
