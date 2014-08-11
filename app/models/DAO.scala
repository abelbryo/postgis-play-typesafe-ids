package models

import play.api.db.slick.DB
import play.api.db.slick._

import play.api.db.slick.Database._
import myUtils.WithMyDriver
import myUtils.MyPostgresDriver

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

class DAO(override val driver: MyPostgresDriver) extends OsmWaysComponent with UsersComponent{
  import driver.simple._

  val osmWays = TableQuery(new OsmWays(_)) // Important for Slick to find the Tables
  val users  = TableQuery(new Users(_))
}

object current {
  val dao = new DAO(DB(play.api.Play.current).driver.asInstanceOf[MyPostgresDriver])
}
