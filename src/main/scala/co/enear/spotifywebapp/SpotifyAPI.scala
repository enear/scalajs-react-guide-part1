package co.enear.spotifywebapp

import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.{JSON, URIUtils}

object SpotifyAPI {

  def fetchArtist(name: String): Future[Option[Artist]] = {
    Ajax.get(artistSearchURL(name)) map { xhr =>
      val searchResults = JSON.parse(xhr.responseText).asInstanceOf[SearchResults]
      searchResults.artists.items.headOption
    }
  }

  def fetchAlbums(artistId: String): Future[Seq[Album]] = {
    Ajax.get(albumsURL(artistId)) map { xhr =>
      val albumListing = JSON.parse(xhr.responseText).asInstanceOf[ItemListing[Album]]
      albumListing.items
    }
  }

  def fetchTracks(albumId: String): Future[Seq[Track]] = {
    Ajax.get(tracksURL(albumId)) map { xhr =>
      val trackListing = JSON.parse(xhr.responseText).asInstanceOf[ItemListing[Track]]
      trackListing.items
    }
  }

  def artistSearchURL(name: String) = s"https://api.spotify.com/v1/search?type=artist&q=${URIUtils.encodeURIComponent(name)}"
  def albumsURL(artistId: String) =   s"https://api.spotify.com/v1/artists/$artistId/albums?limit=50&market=PT&album_type=album"
  def tracksURL(albumId: String) =    s"https://api.spotify.com/v1/albums/$albumId/tracks?limit=50"
}
