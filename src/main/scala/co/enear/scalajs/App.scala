package co.enear.scalajs

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom

object App extends JSApp {

  @JSExport
  override def main(): Unit = {
    ReactDOM.render(TrackListingApp.component(), dom.document.getElementById("playground"))
  }
}
