package repository

import models._
import models.current.dao._

import play.api.db.slick._
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

class UsersRepository extends BaseIdRepository[UserId, User, Users](TableQuery[Users])

object UsersRepository extends UsersRepository {

  def createNewUser(row: User) = DB.withSession {
    implicit session: Session =>
      if (MTable.getTables("USERS").list(session).isEmpty) super.create
      this.save(row)
  }

  def getAllUsers = DB.withSession {
    implicit session: Session =>
      this.findAll
  }

  def getUsersByWayId(wid: WayId) = DB.withSession{
    val result = for {
      w <- OsmWaysComponent.osmWays
      u <- UsersComponent.users if u.wayId === wid
    } yield u

    implicit session: Session =>
      result.list
    }

  }
