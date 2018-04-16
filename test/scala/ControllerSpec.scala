package scala

import controllers.BookController
import models.{ElasticSearchResponse, Page, ParsedResponse}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import play.libs.Json
import services.BookService

import scala.concurrent.Future

class ControllerSpec extends PlaySpec with MockitoSugar{
  "A bookController " should {
  "give correct response for seraching book by book-name and page number " in {
    import scala.concurrent.ExecutionContext.Implicits.global
    val service = mock[BookService]
    val queryMap = Map("book" -> "book1", "page_number" -> "3")
    val resp1 = ParsedResponse("1", Page(1111, 3, "chapter1", "cbwehjfb ewjfc ew", "book1", "author1"))
    val searchResponse = ElasticSearchResponse(Array(resp1))

    when(service.searchPageByQueryMap(queryMap))
      .thenReturn(Future(Option(searchResponse)))

    val controllerUrl = controllers.routes.BookController.
      searchPageByNumberAndBook("book1", 3)
      .url

    val fakeRequest =
      FakeRequest(
        method = Helpers.GET,
        path = controllerUrl
      )


    val controller = new BookController(service, Helpers.stubControllerComponents())
    val response = controller.searchPageByNumberAndBook("book1", 3).apply(
      fakeRequest)

    assert(Json.toJson(searchResponse) == contentAsJson(response))
    assert(200 == status(response))
    assert(true == true)
  }
  }

}
