package scala

import dao.BookDao
import models.Page
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.ws.WSClient
import services.BookService

class ServiceSpec extends PlaySpec with MockitoSugar{
  "Book Service" should {
    "parse response from elastic search properly" in {
      import scala.concurrent.ExecutionContext.Implicits.global
      val elasticSearchResponse = "{\n    " +
        "\"took\": 4,\n    " +
        "\"timed_out\": false,\n    " +
        "\"_shards\": {\n        " +
        "\"total\": 5,\n        " +
        "\"successful\": 5,\n        \"skipped\": 0,\n        \"failed\": 0\n    },\n    \"hits\": {\n        " +
        "\"total\": 1,\n        \"max_score\": 2.1507282,\n        \"hits\": [\n            {\n                \"_index\": \"boogle\",\n                \"_type\": \"page\",\n                \"_id\": \"1\",\n                \"_score\": 2.1507282,\n                \"_source\": {\n                    \"id\": 1,\n                    \"book\": \"The Da Vinci Code\",\n                    \"author\": \"Dan Brown\",\n                    \"page_number\": 3,\n                    \"chapter\": \"Chapter 1\",\n                    " +
        "\"content\": \"Robert Langdon awoke slowly. A telephone was ringing in the darkness-a tinny, unfamiliar ring. He fumbled for the bedside lamp and turned it on. Squinting at his surroundings he saw a plush Renaissance bedroom with Louis XVI furniture, hand-frescoed walls, and a colossal mahogany four-poster bed. Where the hell am I? The jacquard bathrobe hanging on his bedpost bore the monogram: HOTEL RITZ PARIS. Slowly, the fog began to lift.\"\n                }\n            }\n        ]\n    }\n}"

      val dao = mock[BookDao]
      val ws = mock[WSClient]
      val service = new BookService(dao, ws, null)
      val elasticSearchResponseObj = service.parseResponse(elasticSearchResponse)
      assert(elasticSearchResponseObj.get.parsedResponse.length == 1)
      assert(elasticSearchResponseObj.get.parsedResponse(0).id == "1")
      assert(elasticSearchResponseObj.get.parsedResponse(0).page ==
        Page(1,
        3,
        "Chapter 1",
        "Robert Langdon awoke slowly. A telephone was ringing in the darkness-a tinny, unfamiliar ring. He fumbled for the bedside lamp and turned it on. Squinting at his surroundings he saw a plush Renaissance bedroom with Louis XVI furniture, hand-frescoed walls, and a colossal mahogany four-poster bed. Where the hell am I? The jacquard bathrobe hanging on his bedpost bore the monogram: HOTEL RITZ PARIS. Slowly, the fog began to lift.",
        "The Da Vinci Code",
        "Dan Brown"))

    }

    "Parse 0 hits from Elastic Search correctly " in {
      import scala.concurrent.ExecutionContext.Implicits.global
      val elasticSearchResponse = "{\"took\":3,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":0,\"max_score\":null,\"hits\":[]}}"
      val dao = mock[BookDao]
      val ws = mock[WSClient]
      val service = new BookService(dao, ws, null)
      val elasticSearchResponseObj = service.parseResponse(elasticSearchResponse)
      assert(elasticSearchResponseObj.get.parsedResponse.length == 0)
    }
  }

}
