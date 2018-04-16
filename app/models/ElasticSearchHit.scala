package models

import play.api.libs.json.{JsResult, JsSuccess, JsValue, Reads}

object ElasticSearchHit {
  implicit val storyReads = new Reads[ElasticSearchHit] {
    def reads(js: JsValue): JsResult[ElasticSearchHit] = {
      val total = (js \ "total").as[Int]
      val hits = (js \ "hits").as[Array[ParsedResponse]]
      JsSuccess(
        ElasticSearchHit(total, hits)
      )
    }
  }
}

case class ElasticSearchHit(total : Int, parsedResponses: Array[ParsedResponse])
