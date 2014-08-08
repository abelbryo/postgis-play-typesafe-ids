package models

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.geom.LineString
import org.joda.time.LocalDateTime
import play.api.libs.json.JsValue
import myUtils.WithMyDriver

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag


case class WayId(id: Long) extends AnyVal with BaseId

object WayId extends IdCompanion[WayId]


case class OsmWay(
  id: Option[WayId],
  line: LineString
  //version: Int,
  //userId: Int,
  //location: Point,
  //tstamp: LocalDateTime,
  //changeSetId: Int,
  //tags: Map[String, String],
  //nodes: List[Int],
  //others: Option[JsValue]
) extends WithId[WayId] 

trait OsmWaysComponent extends WithMyDriver {
  import driver.simple._

  class OsmWays(tag: Tag) extends IdTable[WayId, OsmWay](tag, "WAYS") {

    def line = column[LineString]("line")

//  def version = column[Int]("version")
//  def userId = column[Int]("user_id")
//  def location = column[Point]("location")
//  def tstamp = column[LocalDateTime]("tstamp")
//  def changeSetId = column[Int]("changeset_id")
//  def tags = column[Map[String, String]]("tags")
//  def nodes = column[List[Int]]("nodes")
//  def others = column[Option[JsValue]]("others")

    // def * = (id, version, userId, location, tstamp, changeSetId, tags, nodes, others) <> (OsmWay.tupled, OsmWay.unapply)

    def * = (id.?, line) <> (OsmWay.tupled, OsmWay.unapply)
  }


}

