package repository

import models._
import models.current.dao.Forums

import play.api.db.slick._
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

class ForumRepository extends BaseIdRepository[ForumId, Forum, Forums](TableQuery[Forums])

object ForumRepository extends ForumRepository {

  def createNewForum(row: Forum) = DB.withSession {
    implicit session: Session =>
      if (MTable.getTables("FORUMS").list(session).isEmpty) super.create
      save(row)
  }

  def getAllForms = DB.withSession {
    implicit session: Session =>
      findAll
  }

  def getForumById(id: ForumId) = DB.withSession {
    implicit session: Session =>
      findById(id)
  }


  def deleteForumById(id: ForumId) = DB.withSession {
    implicit session: Session =>
      deleteById(id)
  }

  def updateForum(forum: Forum) = DB.withSession {
    val query = for { u <- ForumsComponent.forums if u.id === forum.id } yield u
    implicit session: Session =>
      query.update(forum)
  }

}
