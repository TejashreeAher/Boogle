package models

import play.api.libs.json._

case class Page(id: Long, pageNumber: Int, chapter: String, content: String, book: String, author: String)

object Page {
  implicit val storyReads = new Reads[Page] {
    def reads(js: JsValue): JsResult[Page] = {
      val pageId       = (js \ "id").as[Long]
      val pageNumber   = (js \ "page_number").as[Int]
      val chapter      = (js \ "chapter").as[String]
      val content      = (js \ "content").as[String]
      val book         = (js \ "book").as[String]
      val author       = (js \ "author").as[String]
      JsSuccess(
        Page(pageId, pageNumber, chapter, content, book, author)
      )
    }
  }

  implicit def interactionWrites = Writes { (i: Page) =>
    Json.obj(
      "id"          -> i.id,
      "book"        -> i.book,
      "author"      -> i.author,
      "page_number" -> i.pageNumber,
      "chapter"     -> i.chapter,
      "content"     -> i.content
    )
  }
}


