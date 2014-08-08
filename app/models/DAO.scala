package models

import play.api.db.slick.DB
import play.api.db.slick._

import play.api.db.slick.Database._
import myUtils.MyPostgresDriver

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

class DAO(override val driver: MyPostgresDriver) extends OsmWaysComponent {
  import driver.simple._

  val osmWays = TableQuery(new OsmWays(_)) // Important for Slick to find the Tables 
  
  // Important to be able to use the unicorn methods
  class WaysRepository extends BaseIdRepository[WayId, OsmWay, OsmWays](TableQuery[OsmWays])
  object WaysRepository extends WaysRepository

}

object current {
  val dao = new DAO(DB(play.api.Play.current).driver.asInstanceOf[MyPostgresDriver])
}
