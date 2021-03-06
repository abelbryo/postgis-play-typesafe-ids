package repository

import models._
import models.current.dao.OsmWays

import play.api.db.slick.DB
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

// Important to be able to use the unicorn methods
class WaysRepository extends BaseIdRepository[WayId, OsmWay, OsmWays](TableQuery[OsmWays])

object WaysRepository extends WaysRepository {

  def create(row: OsmWay) = DB.withSession {
      implicit session: Session =>
        // check if the table exists
        if (MTable.getTables("WAYS").list(session).isEmpty) super.create
        WaysRepository save row
  }

  def getAllPaths: Seq[OsmWay] = DB.withSession {
      implicit session: Session =>
        this.findAll
    }

}
