package repository

import models._
import models.current.dao.Threads

import play.api.db.slick._
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

class ThreadsRepository extends BaseIdRepository[ThreadId, Thread, Threads](TableQuery[Threads])

object ThreadsRepository extends ThreadsRepository {

  def createNewThread(row: Thread) = DB.withSession {
    implicit session: Session =>
      if (MTable.getTables("THREADS").list(session).isEmpty) super.create
      save(row)
  }

  def getAllThreads = DB.withSession {
    implicit session: Session =>
      findAll
  }

  def getThreadById(id: ThreadId) = DB.withSession {
    implicit session: Session =>
      findById(id)
  }

  def getThreadsByForumId(forumId: ForumId) = DB.withSession {
    val query = for { u <- ThreadsComponent.threads if u.forumId === forumId } yield u
    implicit session: Session =>
      query.list
  }

  def deleteThreadById(id: ThreadId) = DB.withSession {
    implicit session: Session =>
      deleteById(id)
  }

  def updateThread(thread: Thread) = DB.withSession {
    val query = for { u <- ThreadsComponent.threads if u.id === thread.id } yield u
    implicit session: Session =>
      query.update(thread)
  }

}
