package repository

import models._
import models.current.dao._

import play.api.db.slick._
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

// Important to be able to use the unicorn methods
class WaysRepository extends BaseIdRepository[WayId, OsmWay, OsmWays](TableQuery[OsmWays])

object WaysRepository extends WaysRepository {

  def create(row: OsmWay) = {
    DB.withSession {

      implicit session: Session =>

        // check if the table exists or not 
        if (MTable.getTables("WAYS").list(session).isEmpty) super.create

        WaysRepository save row

    }
  }

  def getAllPaths: Seq[OsmWay] = {
    DB.withSession {
      implicit session: Session =>
        this.findAll
    }
  }

}
