package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.impl._
import org.joda.time.DateTime

import play.api.data._
import play.api.data.Forms._

import models._
import repository._
import scala.util.{ Try, Success, Failure }

object ForumController extends Controller {

  case class ForumHelper(name: String)
  case class MessageHelper(id: Long, parentId: Long, title: String, body: String)

  val forumForm = Form(mapping(
    "name" -> nonEmptyText)(ForumHelper.apply)(ForumHelper.unapply))

  val messageForm = Form(mapping(
    "user_id" -> longNumber,
    "reply_to" -> longNumber,
    "title" -> nonEmptyText,
    "body" -> nonEmptyText)(MessageHelper.apply)(MessageHelper.unapply))

  def showForms = Action { implicit request =>
    Ok(views.html.forum.index(forumForm, messageForm))
  }

  def submitForum = Action { implicit request =>
    forumForm.bindFromRequest.fold(error => {
      BadRequest(views.html.forum.index(error, messageForm))
    },
      data => {
        val forumHelper: ForumHelper = data
        val forum = Forum(None, forumHelper.name, new DateTime)

        // create a thread also for later use

        Try(ForumRepository.createNewForum(forum)) match {
          case Success(forumId) =>
            play.Logger.debug(s"Success! Forum created with title -- [ ${forumHelper.name} ] ++  ${forumId}  ++  --")
            val thread = Thread(None, forumId, new DateTime)

            Try(ThreadsRepository.createNewThread(thread)) match {
              case Success(threadId) =>
                play.Logger.debug(s"Success! Thread created with id -- ++ ${threadId}  ++  --")
              case f @ Failure(e) => play.Logger.debug("Failed " + e.toString)
            }
          case f @ Failure(e) => play.Logger.debug("Failed " + e.toString)
        }

      })

    Ok("Submitted Forum name")
  }

  def submitMessage = Action { implicit request =>

    messageForm.bindFromRequest.fold(error => {
      BadRequest(views.html.forum.index(forumForm, error))
    },
      data => {
        val messageHelper: MessageHelper = data

        val forum = ForumRepository.getForumById(ForumId(2))

        val thread = ThreadsRepository.getThreadById(ThreadId(1))

        val parentId = if (messageHelper.parentId == 0) None else Option[MessageId](MessageId(messageHelper.parentId))

        val message = Message(None,
          messageHelper.title,
          messageHelper.body,
          thread.get.id.get,
          parentId,
          UserId(messageHelper.id),
          new DateTime)

        Try(MessageRepository.createNewMessage(message)) match {
          case Success(s) => play.Logger.debug(s"Success! Forum created with title -- [ ${message.title} ] --")
          case f @ Failure(e) => play.Logger.debug("Failed " + e.toString)
        }

      })

    Ok("Submitted message. thanks!")
  }

  def index = Action { implicit rs =>
    val allPaths = WaysRepository.getAllPaths.asInstanceOf[List[OsmWay]]
    val allUsers = UsersRepository.getAllUsers.asInstanceOf[List[User]]
    val usersById = UsersRepository.getUsersByWayId(WayId(1)).asInstanceOf[List[User]]

    Ok(views.html.index(allPaths, allUsers, usersById))
  }

  def createUserAndInsert = Action { implicit rs =>

    val carr: Array[Coordinate] = Array(new Coordinate(10, 10), new Coordinate(20, 20), new Coordinate(40, 40))
    val cs = new CoordinateArraySequence(carr)
    val gf = new GeometryFactory(new PrecisionModel(), 4326)
    val line = new LineString(cs, gf)
    val way = OsmWay(None, line)

    for (i <- 1 to 3) WaysRepository.create(way)

    val user1 = User(None, "Doug Mc", WayId(1))
    val user2 = User(None, "George Tr", WayId(2))
    val user3 = User(None, "Tray Hk", WayId(3))

    UsersRepository.createNewUser(user1)
    UsersRepository.createNewUser(user2)
    UsersRepository.createNewUser(user3)

    Redirect(routes.Application.index)
  }

  def updateUser = Action { implicit request =>
    val oldUser = UsersRepository.getUserById(UserId(1))
    oldUser.map { old =>
      val newUser = User(old.id, "Haile Gebresillasie", WayId(2))
      UsersRepository.updateUser(newUser)
    }
    Ok("Done")
  }

}
