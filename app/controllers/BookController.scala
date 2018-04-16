package controllers

import javax.inject.Inject

import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.BookService

import scala.concurrent.ExecutionContext

class BookController @Inject()(service: BookService,
                               cc: ControllerComponents)
                              (implicit ec: ExecutionContext) extends AbstractController(cc){

  val LOGGER = LoggerFactory.getLogger(this.getClass.getName)

  /**
    * Get the page number of the given book from database, this doesn't call elastic search. This calls DB directly
    *
    * @param bookName - The name of the book to search page in (For now, the exact and complete name of the book is required)
    * @param pageNumber - Pagenumber to serach for in book $bookName
    * @return - Json representing details of the page with its content
    */
  def getPageByNumber(bookName: String, pageNumber : Int)= Action{ request =>
    Ok(Json.toJson(service.getPageByNumber(bookName, pageNumber)))
  }

  /**
    * Get the page number of the given book the indexed pages
    *
    * @param book - The name of the book to search page in (For now, the exact and complete name of the book is required)
    * @param number - Page number to serach for in book $bookName
    * @return - Json representing details of the page with its content
    */
  def searchPageByNumberAndBook(book: String, number: Int) = Action.async{ request =>
      val queryMap = Map("book" -> book, "page_number" -> number.toString)
      service.searchPageByQueryMap(queryMap)
        .map{
          result => {
            result match {
              case Right(r) => {
                r match {
                  case None => Ok("[]")
                  case Some(r) => Ok(Json.toJson(r))
                }
              }
              case Left(r) => {
                InternalServerErrorResponse(r.get)
              }
            }
          }
        }
  }

  /**
    * get the pages in which the given text appears in the indexed pages
    *
    * @param text - Text to be searched, the search is case-insensitive
    * @return - Json array of the pages with its content
    */
  def searchPageByText(text: String) = Action.async{ request =>
    val queryMap = Map("content" -> text)
    service.searchPageByQueryMap(queryMap)
      .map{
        result => {
          result match {
            case Right(r) => {
              r match {
                case None => Ok("[]")
                case Some(r) => Ok(Json.toJson(r))
              }
            }
            case Left(r) => InternalServerErrorResponse(r.get)
          }
        }
      }

  }

  /**
    * indexes the page with the give id (in the database). The same id is used in the index too
    *
    * @param id - page id of the page in the database, and the id with which the index is created
    * @return - String in case of success and excpetion in case of error
    */
  def indexPage(id: Int) = Action.async {request =>
    service.indexPageById(id)
      .map{result =>
        result match {
          case true => Ok(s"Indexed page with id : ${id}")
          case false => InternalServerError
        }
      }
  }

  private def badRequest(errorMsg : String) = {
      BadRequest(
        Json.obj(
          "error" -> errorMsg
        ))
  }

  private def InternalServerErrorResponse(errorMsg : String) = {
    InternalServerError(Json.obj(
      "error" -> errorMsg
    ))
  }

}
