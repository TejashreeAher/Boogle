package models

import play.api.libs.json._

/** **
  * {
  * "took":8,"timed_out":false,
  * "_shards":{"total":5,"successful":5,"skipped":0,"failed":0},
  * "hits":{
  * "total":1,"max_score":1.7594807,"hits":
  * [
  * {"_index":"boogle","_type":"page","_id":"AWLJwfx-V0BXkAJIyG06","_score":1.7594807,
  * "_source":
  * {
    *"page_number" : 1,
    *"book" : "Da Vinci Code",
    *"author" : "Dan Brown",
    *"content" : "This book is dedicated to my family"
*}}]}}
 *
 */

object ElasticSearchResponse {
  implicit val storyReads = new Reads[ElasticSearchResponse] {
    def reads(js: JsValue): JsResult[ElasticSearchResponse] = {
//      val searchHit = (js \ "hits").as[ElasticSearchHit]
      val searchHit = Json.parse((js \ "hits").get.toString())
      val parsedResponse = (searchHit \ "hits").as[Array[ParsedResponse]]
      JsSuccess(
        ElasticSearchResponse(parsedResponse)
      )
    }
  }


  implicit def interactionWrites = Writes { (i: ElasticSearchResponse) =>
    Json.toJson(i.parsedResponse)
  }
}

case class ElasticSearchResponse(parsedResponse: Array[ParsedResponse])
