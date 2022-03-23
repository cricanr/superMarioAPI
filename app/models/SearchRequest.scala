package models

import play.api.libs.json.{Json, Reads}

case class SearchRequest(names: List[String])

object SearchRequest {
  implicit val searchRequestReads: Reads[SearchRequest] =
    Json.reads[SearchRequest]
}
