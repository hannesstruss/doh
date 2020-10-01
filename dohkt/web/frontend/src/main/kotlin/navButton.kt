import kotlinx.css.VerticalAlign
import kotlinx.css.em
import kotlinx.css.height
import kotlinx.css.padding
import kotlinx.css.verticalAlign
import kotlinx.css.width
import kotlinx.html.BUTTON
import styled.StyledDOMBuilder
import styled.css

fun StyledDOMBuilder<BUTTON>.navButtonStyles() {
  css {
    height = 3.em
    width = 3.em
    verticalAlign = VerticalAlign.top
  }
}
