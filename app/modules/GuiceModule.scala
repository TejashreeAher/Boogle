package modules

import java.sql.Connection

import com.google.inject.AbstractModule
import dao.BookDao

class GuiceModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[Connection]).toProvider(classOf[DBConnectionProvider]).asEagerSingleton()
    bind(classOf[AppConfig]).asEagerSingleton()
    bind(classOf[BookDao]).asEagerSingleton()
//    bind(classOf[DataLoader]).asEagerSingleton()
  }

}
