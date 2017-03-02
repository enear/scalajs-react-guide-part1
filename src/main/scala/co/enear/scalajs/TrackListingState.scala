package co.enear.scalajs

case class TrackListingState(
  artistInput: String,
  albums: Seq[Album],
  tracks: Seq[Track]
)

object TrackListingState {
  val empty = TrackListingState("", Nil, Nil)
}