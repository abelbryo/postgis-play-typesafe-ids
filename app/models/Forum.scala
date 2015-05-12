package models

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag
import org.joda.time.DateTime

import myUtils.WithMyDriver

case class ForumId(id: Long) extends AnyVal with BaseId

object ForumId extends IdCompanion[ForumId]

case class Forum(id: Option[ForumId],
  name: String,
  timestamp: DateTime) extends WithId[ForumId]

trait ForumsComponent extends WithMyDriver {
  import driver.simple._

  class Forums(tag: Tag) extends IdTable[ForumId, Forum](tag, "FORUMS") {
    def name = column[String]("FORUM_NAME")
    def timestamp = column[DateTime]("CREATED_ON")

    def * = (id.?, name, timestamp) <> (Forum.tupled, Forum.unapply)
  }

}

object ForumsComponent extends ForumsComponent {
  val forums = TableQuery[Forums]
  override val driver = myUtils.MyPostgresDriver
}
