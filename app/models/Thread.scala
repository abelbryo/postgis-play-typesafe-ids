package models

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag

import org.joda.time.DateTime

import myUtils.WithMyDriver

case class ThreadId(id: Long) extends AnyVal with BaseId

object ThreadId extends IdCompanion[ThreadId]

case class Thread(id: Option[ThreadId],
  forumId: ForumId,
  timestamp: DateTime
) extends WithId[ThreadId]

trait ThreadsComponent extends WithMyDriver {
  import driver.simple._

  class Threads(tag: Tag) extends IdTable[ThreadId, Thread](tag, "THREADS") {
    def forumId  = column[ForumId]("FORUM_ID")

    def forum = foreignKey("THREAD_FORUM_FK", forumId, ForumsComponent.forums)(_.id)

    def timestamp = column[DateTime]("CREATED_ON")

    def * = (id.?, forumId, timestamp) <> (Thread.tupled, Thread.unapply)
  }

}

object ThreadsComponent extends ThreadsComponent {
  val threads = TableQuery[Threads]
  override val driver = myUtils.MyPostgresDriver
}
