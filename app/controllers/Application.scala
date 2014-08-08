package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.slick._
import play.api.db.slick.{ Session => MyS }
import play.api.Play.current
import myUtils._
import models._
import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.impl._

//stable imports to use play.api.Play.current outside of objects:
import models.current.dao._
// import repository.current.dao.driver.simple._
// import repository.current.dao.driver.simple.{Session => MyDBSession }
import repository.QueryMethods._

object Application extends Controller {

  def index = Action { implicit rs =>
    // val list = repo findAll
    Ok(views.html.index(Nil))
  }


  def insert = Action { implicit rs =>

        val carr: Array[Coordinate] = Array(new Coordinate(10, 10), new Coordinate(20, 20), new Coordinate(40, 40))
        val cs = new CoordinateArraySequence(carr)

        val gf = new GeometryFactory(new PrecisionModel(), 4326)

        val line = new LineString(cs, gf)
        val way = OsmWay(None, line)
        val wayId: WayId = create(way)


   Redirect(routes.Application.index)
  }

}
