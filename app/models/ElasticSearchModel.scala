package models

import play.api.libs.json._

case class ElasticSearchModel(queryParams : Map[String, String])

object ElasticSearchModel {

  def getSearchParameters(query: Map[String, String]): Json.JsValueWrapper = {
    val jsonArray = query.map{case(key, value) => {
      Json.obj("match" -> Json.obj(
        key -> value
      ))
    }}
    jsonArray
  }

  implicit def interactionWrites = Writes { (input: ElasticSearchModel) =>
    Json.obj(
      "query" -> Json.obj(
        "bool" -> Json.obj(
          "must" -> Json.arr(
            getSearchParameters(input.queryParams)
          )
        )
      )
    )
  }
}
 /**
   * {
    "query": {
        "bool" : {
            "must" : [
            	{
            		"match": {
            			"book" : "Da Vinci Code"
            		}
            	}, {
            		"match" : {
            			"page_number" :  1
            		}
            	}

                ]
        }
    }
}
   * */