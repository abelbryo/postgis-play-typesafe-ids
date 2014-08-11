package models

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag

import myUtils.WithMyDriver

case class UserId(id: Long) extends AnyVal with BaseId

object UserId extends IdCompanion[UserId]

case class User(id: Option[UserId],
  name: String,
  wayId: WayId) extends WithId[UserId]

trait UsersComponent extends WithMyDriver {
  import driver.simple._

  class Users(tag: Tag) extends IdTable[UserId, User](tag, "USERS") {
    def name = column[String]("FULL_NAME")
    def wayId = column[WayId]("WAY_ID")

    def way = foreignKey("USER_WAY_FK", wayId, OsmWaysComponent.osmWays)(_.id)

    def * = (id.?, name, wayId) <> (User.tupled, User.unapply)
  }

}

object UsersComponent extends UsersComponent {
  val users = TableQuery[Users]
  override val driver = myUtils.MyPostgresDriver
}
