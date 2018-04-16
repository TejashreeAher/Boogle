package modules

import javax.inject.{Inject, Singleton}

import services.BookService

import scala.concurrent.ExecutionContext

@Singleton
class DataLoader @Inject()(service: BookService)
                          (implicit ec: ExecutionContext){

  val areAllPagesIndexed = indexAllPages()

  def indexAllPages() = {
      service.indexAllPages()
  }

}
