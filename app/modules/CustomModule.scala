package modules

import com.google.inject.AbstractModule

/*
This class loads all the data form database, and indexes all the pages. It runs on startup of the service
* */
class CustomModule extends AbstractModule {
  override def configure() = {
    bind(classOf[DataLoader]).asEagerSingleton()
  }
}
