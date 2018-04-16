package modules

import javax.inject.{Inject, Singleton}

import com.typesafe.config.Config
import play.api.{Application, ConfigLoader}

@Singleton
class AppConfig @Inject()(implicit val current: Application) {
  val ELASTIC_SEARCH_CONFIG = current.configuration.get[ElasticSearchConfig]("elastic_search")
}

case class ElasticSearchConfig(host: String, port: Int, index: String)

object ElasticSearchConfig {

  implicit val configLoader: ConfigLoader[ElasticSearchConfig] = new ConfigLoader[ElasticSearchConfig] {
    def load(rootConfig: Config, path: String): ElasticSearchConfig = {
      val config = rootConfig.getConfig(path)
      ElasticSearchConfig(
        host = config.getString("host"),
        port = config.getInt("port"),
        index = config.getString("index")
      )
    }
  }
}
