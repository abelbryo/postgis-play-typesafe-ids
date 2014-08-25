package repository

import models._
import models.current.dao.Piles  // Table

import play.api.db.slick.DB
import play.api.Play.current
import play.api.libs.json._

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

import com.vividsolutions.jts.geom._

// Important to be able to use the unicorn methods
class PilesRepository extends BaseIdRepository[PileId, Pile, Piles](TableQuery[Piles])

object PilesRepository extends PilesRepository {
  import myUtils.MyPostgresDriver.simple._
  import models.current.dao.piles  // TableQuery

  def create(row: Pile) = DB.withSession {
    implicit session: Session =>
      // check if the table exists
      if (MTable.getTables("WOODS").list(session).isEmpty) super.create
      PilesRepository save row
  }

  def getAllPaths: Seq[Pile] = DB.withSession {
    implicit session: Session =>
      this.findAll
  }

  def getPileById(id: PileId): Option[Pile] = DB.withSession {
    implicit session: Session =>
      this.findById(id)
  }

  /* All geom within d distance from point p */
  def byDistance(point: Point, distance: Double) = DB.withSession {
    implicit session: Session =>
      piles
        .filter(r => r.geometry.dWithin(point.bind, distance.bind))
        .map(t => t)
  }

  def asGeoJSON(pileId: PileId): Option[JsValue] = DB.withSession{
    implicit session: Session =>
      val geojson = piles.filter(_.id === pileId).map(_.geometry.asGeoJSON())
      geojson.firstOption.map(e => Json.toJson(e))
  }

  def geomByOwnerId(userId: UserId): Seq[String] = DB.withSession{
    implicit session: Session =>
      val query = piles.filter(_.ownerId === userId).map(_.geometry.asGeoJSON())
      query.list
  }

  def __geomByOwnerIdTesting(userId: UserId)  = DB.withSession{
    implicit session: Session =>
      val query = piles.filter(_.ownerId === userId).map(e => ( e.geometry.asGeoJSON(), e.wood_id, e.tunnus  ))
      query.list
  }

}
