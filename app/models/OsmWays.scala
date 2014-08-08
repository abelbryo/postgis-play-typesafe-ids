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
  line: LineString) extends WithId[WayId]

trait OsmWaysComponent extends WithMyDriver {
  import driver.simple._

  class OsmWays(tag: Tag) extends IdTable[WayId, OsmWay](tag, "WAYS") {

    def line = column[LineString]("line")

    def * = (id.?, line) <> (OsmWay.tupled, OsmWay.unapply)
  }
}

