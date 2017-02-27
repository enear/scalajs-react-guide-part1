package co.enear.scalajs

case class TrackListingState(
  artist: ArtistInput,
  album: AlbumInput,
  tracks: TrackOutput
)

case class ArtistInput(
  text: String,
  selected: Option[Artist]
)

case class AlbumInput(
  albums: Seq[Album],
  selected: Option[Album]
)

case class TrackOutput(
  tracks: Seq[Track]
)

object TrackListingState {
  val empty = TrackListingState(
    ArtistInput("", None),
    AlbumInput(Nil, None),
    TrackOutput(Nil)
  )
}