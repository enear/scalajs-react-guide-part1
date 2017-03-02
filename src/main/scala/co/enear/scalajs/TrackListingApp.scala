package co.enear.scalajs

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.raw.HTMLSelectElement
import org.scalajs.dom.window

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TrackListingApp {

  val component = ReactComponentB[Unit]("Spotify Track Listing")
    .initialState(TrackListingState.empty)
    .renderBackend[TrackListingOps]
    .build

  class TrackListingOps($: BackendScope[Unit, TrackListingState]) {

    val artistInputState = $.zoom(_.artistInput)((s, x) => s.copy(artistInput = x))
    val albumsState = $.zoom(_.albums)((s, x) => s.copy(albums = x))
    val tracksState = $.zoom(_.tracks)((s, x) => s.copy(tracks = x))

    def updateArtistInput(event: ReactEventI): Callback = {
      event.persist()
      artistInputState.setState(event.target.value)
    }

    def searchForArtist(name: String) = Callback.future {
      for {
        artistOpt <- SpotifyAPI.fetchArtist(name)
        albums <- artistOpt map (artist => SpotifyAPI.fetchAlbums(artist.id)) getOrElse Future.successful(Nil)
        tracks <- albums.headOption map (album => SpotifyAPI.fetchTracks(album.id)) getOrElse Future.successful(Nil)
      } yield {
        artistOpt match {
          case None => Callback(window.alert("No artist found"))
          case Some(artist) => $.setState(TrackListingState(artist.name, albums, tracks))
        }
      }
    }

    def updateTracks(event: ReactEventI) = Callback.future {
      val albumId = event.target.asInstanceOf[HTMLSelectElement].value
      SpotifyAPI.fetchTracks(albumId) map { tracks => tracksState.setState(tracks) }
    }

    def formatDuration(timeMs: Int): String = {
      val timeSeconds = Math.round(timeMs / 1000.0)
      (timeSeconds / 60) + ":" + "%02d".format(timeSeconds % 60)
    }

    def render(s: TrackListingState) = {
      <.div(^.cls := "container",
        <.h1("Spotify Track Listing"),
        <.div(^.cls := "form-group",
          <.label(^.`for` := "artist", "Artist"),
          <.div(^.cls := "row", ^.id := "artist",
            <.div(^.cls := "col-xs-10",
              <.input(^.`type` := "text", ^.cls := "form-control",
                ^.value := s.artistInput, ^.onChange ==> updateArtistInput
              )
            ),
            <.div(^.cls := "col-xs-2",
              <.button(^.`type` := "button", ^.cls := "btn btn-primary custom-button-width",
                ^.onClick --> searchForArtist(s.artistInput),
                ^.disabled := s.artistInput.isEmpty,
                "Search"
              )
            )
          )
        ),
        <.div(^.cls := "form-group",
          <.label(^.`for` := "album", "Album"),
          <.select(^.cls := "form-control", ^.id := "album",
            ^.onChange ==> updateTracks,
            s.albums.map { album =>
              <.option(^.value := album.id, album.name)
            }
          )
        ),
        <.hr,
        <.ul(s.tracks map { track =>
          <.li(
            <.div(
              <.p(s"${track.track_number}. ${track.name} (${formatDuration(track.duration_ms)})"),
              <.audio(^.controls := true, ^.key := track.preview_url,
                <.source(^.src := track.preview_url)
              )
            )
          )
        })
      )
    }

  }

}
