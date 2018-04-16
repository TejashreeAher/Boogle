package models

import play.api.libs.json._

case class ParsedResponse(id: String, page: Page)

object ParsedResponse {
  implicit val storyReads = new Reads[ParsedResponse]{
    def reads(js: JsValue): JsResult[ParsedResponse] = {
      val id = (js \ "_id").as[String]
      val response = (js \ "_source").as[Page]
      JsSuccess(ParsedResponse(id, response))
    }
  }

  implicit def interactionWrites = Writes { (i: ParsedResponse) =>
    Json.obj(
      "search_id"     -> i.id,
      "page"          -> i.page
    )
  }
}