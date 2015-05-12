package models

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag

import org.joda.time.DateTime

import myUtils.WithMyDriver

case class MessageId(id: Long) extends AnyVal with BaseId

object MessageId extends IdCompanion[MessageId]

case class Message(id: Option[MessageId],
  title: String,
  body: String,
  threadId: ThreadId,
  parentId: Option [ MessageId ],
  userId:  UserId,
  timestamp: DateTime) extends WithId[MessageId]

trait MessagesComponent extends WithMyDriver {
  import driver.simple._

  class Messages(tag: Tag) extends IdTable[MessageId, Message](tag, "MESSAGES") {
    def title = column[String]("TITLE")
    def body = column[String]("BODY")

    def threadId = column[ThreadId]("THREAD_ID")
    def thread = foreignKey("MESSAGE_THREAD_FK", threadId, ThreadsComponent.threads)(_.id)

    def parentId = column[Option[ MessageId ]]("PARENT_ID")
    def parent = foreignKey("MESSAGE_PARENT_FK", parentId, MessagesComponent.messages)(_.id)

    def userId = column[ UserId ]("USER_ID")
    def user = foreignKey("MESSAGE_USER_FK", userId, UsersComponent.users)(_.id)

    def timestamp = column[DateTime]("CREATED_ON")

    def * = (id.?, title, body, threadId, parentId, userId, timestamp) <> (Message.tupled, Message.unapply)
  }

}

object MessagesComponent extends MessagesComponent {
  val messages = TableQuery[Messages]
  override val driver = myUtils.MyPostgresDriver
}
