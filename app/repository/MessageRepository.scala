package repository

import models._
import models.current.dao.Messages

import play.api.db.slick._
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

class MessageRepository extends BaseIdRepository[MessageId, Message, Messages](TableQuery[Messages])

object MessageRepository extends MessageRepository {

  def createNewMessage(row: Message) = DB.withSession {
    implicit session: Session =>
      if (MTable.getTables("MESSAGES").list(session).isEmpty) super.create
      save(row)
  }

  def getAllMessages = DB.withSession {
    implicit session: Session =>
      findAll
  }

  def getMessageById(id: MessageId) = DB.withSession {
    implicit session: Session =>
      findById(id)
  }

  def getMessagesByThreadId(threadId: ThreadId) = DB.withSession {
    val query = for { u <- MessagesComponent.messages if u.threadId === threadId } yield u
    implicit session: Session =>
      query.list
  }

  def getMessagesByParentId(parentId: MessageId) = DB.withSession {
    val query = for { u <- MessagesComponent.messages if u.parentId === parentId } yield u
    implicit session: Session =>
      query.list
  }

  def getMessagesByUserId(userId: UserId) = DB.withSession {
    val query = for { u <- MessagesComponent.messages if u.userId === userId } yield u
    implicit session: Session =>
      query.list
  }


  def deleteMessageById(id: MessageId) = DB.withSession {
    implicit session: Session =>
      deleteById(id)
  }

  def updateMessage(message: Message) = DB.withSession {
    val query = for { u <- MessagesComponent.messages if u.id === message.id } yield u
    implicit session: Session =>
      query.update(message)
  }

}
