package services

import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import dao.BookDao
import models.{ElasticSearchModel, ElasticSearchResponse, Page}
import modules.AppConfig
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.Duration


@Singleton
class BookService @Inject()(dao: BookDao,
                            ws: WSClient,
                            config: AppConfig)
                           (implicit ec: ExecutionContext){
  val LOGGER = LoggerFactory.getLogger(this.getClass.getName)

  def getPageByNumber(bookName: String, pageNumber : Int): List[Page]={
    dao.getPageByNumber(bookName, pageNumber)
  }

  def getPageById(id: Long): Option[Page] = {
    dao.getPageById(id)
  }

  def indexAllPages() = {
    val pages = dao.getAllPages()
    pages.map{x => indexPage(x)}
  }

  def indexPageById(id : Long)={
    val page = getPageById(id)
    page match{
      case Some(p) => indexPage(p)
      case None    => throw PageNotFoundException(s"Page with id ${id} not found to index")
    }
  }

  def indexPage(page: Page): Future[Boolean] = {
    val requestBody = Json.toJson(page)

    val request = ws.url(s"http://${config.ELASTIC_SEARCH_CONFIG.host}:${config.ELASTIC_SEARCH_CONFIG.port}/${config.ELASTIC_SEARCH_CONFIG.index}/page/${page.id}")
      .addHttpHeaders("Content-Type" -> "application/json")
      .withRequestTimeout(Duration.create(1000, TimeUnit.MILLISECONDS))
      .put(requestBody)

    request.map {
      response => {
        val parsedResponse = response.status match {
          case 201 => {
            ///success
            true
          }
          case 200 => {
            ///success
            true
          }
          case _ => {
            LOGGER.info(s"status is ${response.status}")
            throw SearchException("Search Exception")
          }
        }
        parsedResponse
      }
    }
  }

  def searchPageByQueryMap(queryMap : Map[String, String]): Future[Either[Option[String],Option[ElasticSearchResponse]]] = {
    val requestBody = Json.toJson(ElasticSearchModel(queryMap))
    val request = ws.url(s"http://${config.ELASTIC_SEARCH_CONFIG.host}:${config.ELASTIC_SEARCH_CONFIG.port}/${config.ELASTIC_SEARCH_CONFIG.index}/_search")
      .addHttpHeaders("Content-Type" -> "application/json")
      .withRequestTimeout(Duration.create(1000, TimeUnit.MILLISECONDS))
      .post(requestBody)

    request.map {
      response => {
        val parsedResponse = response.status match {
          case 200 => {
            ///success
            Right(parseResponse(response.body))
          }
          case 404 => {
            LOGGER.info("404 found ")
//            throw new RuntimeException("Index not found ")
            Left(Option("Index not found"))
          }
          case _ => {
//            throw new RuntimeException("Search Exception")
            Left(Option("Error Searching index"))
          }
        }
        parsedResponse
      }
    }
  }

  def parseResponse(response: String): Option[ElasticSearchResponse] = {
    response.trim.length match{
      case 0 => None
      case _ => {
        val responseJson = Json.parse(response)
        Option(responseJson.as[ElasticSearchResponse])
      }
    }
  }
}


case class PageNotFoundException(message : String) extends  RuntimeException
case class SearchException(message : String) extends RuntimeException
