package models

import play.api.libs.json.{Json, Reads, Writes}

case class SearchRequest(names: List[String])

object SearchRequest {
  implicit val searchRequestReads: Reads[SearchRequest] =
    Json.reads[SearchRequest]

  implicit val searchRequestWrites: Writes[SearchRequest] =
    Json.writes[SearchRequest]

}
