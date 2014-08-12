package repository

import models._
import models.current.dao.Users

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
      save(row)
  }

  def getAllUsers = DB.withSession {
    implicit session: Session =>
      findAll
  }

  def getUserById(id: UserId) = DB.withSession {
    implicit session: Session =>
      findById(id)
  }

  def getUsersByWayId(wid: WayId) = DB.withSession {
    val query = for { u <- UsersComponent.users if u.wayId === wid } yield u
    implicit session: Session =>
      query.list
  }

  def deleteUserById(id: UserId) = DB.withSession {
    implicit session: Session =>
      deleteById(id)
  }

  def updateUser(user: User) = DB.withSession {
    val query = for { u <- UsersComponent.users if u.id === user.id } yield u
    implicit session: Session =>
      query.update(user)
  }

}
