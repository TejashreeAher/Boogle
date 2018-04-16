package dao

import javax.inject.{Inject, Singleton}

import models.Page
import play.api.db.Database

@Singleton
class BookDao @Inject()(db: Database){

    def getPageByNumber(bookName: String, pageNumber : Int) ={
        val conn = db.getConnection
        try {
            val stmt = conn.createStatement
            val rs = stmt.executeQuery(s"SELECT p.id, p.page_number, p.chapter, p.content, b.title, b.author from Pages p left join Books b on p.book_id=b.id where p.page_number = ${pageNumber} and b.title = '${bookName}' ")
            var listOfPages: List[Page] = List()
            while (rs.next()) {
                listOfPages = listOfPages.::(Page(rs.getLong("id"), rs.getInt("page_number"), rs.getString("chapter"), rs.getString("content"), rs.getString("title"), rs.getString("author")))
            }
            listOfPages

        } finally {
            conn.close()
        }
    }

    def getPageById(id: Long): Option[Page] = {
        val conn = db.getConnection
        try {
            val stmt = conn.createStatement
            val rs = stmt.executeQuery(s"SELECT p.id, p.page_number, p.chapter, p.content, b.title, b.author from Pages p left join Books b on p.book_id=b.id where p.id = ${id} ")
            rs.next() match {
                case true =>  Option(Page(rs.getLong("id"), rs.getInt("page_number"), rs.getString("chapter"), rs.getString("content"), rs.getString("title"), rs.getString("author")))
                case false => None
            }


        } finally {
            conn.close()
        }
    }

    def getAllPages(): List[Page] = {
        val conn = db.getConnection
        try {
            val stmt = conn.createStatement
            val rs = stmt.executeQuery(s"SELECT p.id, p.page_number, p.chapter, p.content, b.title, b.author from Pages p left join Books b on p.book_id=b.id")
            var listOfPages: List[Page] = List()
            while (rs.next()) {
                listOfPages = listOfPages.::(Page(rs.getLong("id"), rs.getInt("page_number"), rs.getString("chapter"), rs.getString("content"), rs.getString("title"), rs.getString("author")))
            }
            listOfPages

        } finally {
            conn.close()
        }
    }

}
