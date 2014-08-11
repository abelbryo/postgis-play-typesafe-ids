package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current
import myUtils._
import models._
import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.impl._

//stable imports to use play.api.Play.current outside of objects:
import models.current.dao._
import repository._

object Application extends Controller {

  def index = Action { implicit rs =>
    val allPaths = WaysRepository.getAllPaths.asInstanceOf[List[OsmWay]]
    val allUsers = UsersRepository.getAllUsers.asInstanceOf[List[User]]

    Ok(views.html.index(allPaths, allUsers))
  }

  def insert = Action { implicit rs =>

    val carr: Array[Coordinate] = Array(new Coordinate(10, 10), new Coordinate(20, 20), new Coordinate(40, 40))
    val cs = new CoordinateArraySequence(carr)
    val gf = new GeometryFactory(new PrecisionModel(), 4326)
    val line = new LineString(cs, gf)
    val way = OsmWay(None, line)

    for (i <- 1 to 3)
      WaysRepository.create(way)

    val user1 = User(None, "Doug Mc", WayId(1))
    val user2 = User(None, "George Tr", WayId(2))
    val user3 = User(None, "Tray Hk", WayId(3))

    UsersRepository.createNewUser(user1)
    UsersRepository.createNewUser(user2)
    UsersRepository.createNewUser(user3)

    Redirect(routes.Application.index)
  }

}
