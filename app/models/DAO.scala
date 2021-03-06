package models

import play.api.db.slick.DB
import play.api.db.slick._

import play.api.db.slick.Database._
import myUtils.WithMyDriver
import myUtils.MyPostgresDriver

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

class DAO(override val driver: MyPostgresDriver) extends OsmWaysComponent
  with PilesComponent
  with UsersComponent
  with ThreadsComponent
  with ForumsComponent
  with MessagesComponent {

  import driver.simple._

  val osmWays = TableQuery(new OsmWays(_)) // Important for Slick to find the Tables
  val users = TableQuery(new Users(_))
  val piles = TableQuery(new Piles(_))
  val threads = TableQuery(new Threads(_))
  val forums = TableQuery(new Forums(_))
  val messages = TableQuery(new Messages(_))
}

object current {
  val dao = new DAO(DB(play.api.Play.current).driver.asInstanceOf[MyPostgresDriver])
}
