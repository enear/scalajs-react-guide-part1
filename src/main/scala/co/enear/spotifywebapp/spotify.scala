package co.enear.spotifywebapp

import scala.scalajs.js

@js.native
trait SearchResults extends js.Object {
  def artists: ItemListing[Artist]
}

@js.native
trait ItemListing[T] extends js.Object {
  def items: js.Array[T]
}

@js.native
trait Artist extends js.Object {
  def id: String
  def name: String
}

@js.native
trait Album extends js.Object {
  def id: String
  def name: String
}

@js.native
trait Track extends js.Object {
  def id: String
  def name: String
  def track_number: Int
  def duration_ms: Int
  def preview_url: String
}