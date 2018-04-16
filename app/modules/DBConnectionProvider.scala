package modules

import java.sql.Connection
import javax.inject.Inject

import com.google.inject.Provider
import play.db._


class DBConnectionProvider @Inject()(db: Database) extends Provider[Connection]{

  override def get(): Connection = {
    db.getConnection()
  }
}
