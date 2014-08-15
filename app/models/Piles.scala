package models

import com.vividsolutions.jts.geom._
import org.joda.time.LocalDateTime

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag

import myUtils.WithMyDriver

case class PileId(id: Long) extends AnyVal with BaseId

object PileId extends IdCompanion[PileId]

case class Pile(id: Option[PileId],
  wkb_geometry: Geometry,
  wood_id: Option[ Long ],
  tunnus: String,
  kunta: Long,
  alue: Long,
  lohko: Long,
  mts: Double,
  kuvio: Double,
  alakuvio: Option[String],
  pinta_ala: Double,
  ryhma: Double,
  luokka: Double,
  aika: Option[ String ],
  teksti: Option[ String ],
  ownerId: UserId) extends WithId[PileId]

trait PilesComponent extends WithMyDriver {
  import driver.simple._

  class Piles(tag: Tag) extends IdTable[PileId, Pile](tag, "WOODS") {
    def wkb_geometry = column[Geometry]("wkb_geometry")
    def wood_id = column[Option[Long]]("wood_id")
    def tunnus = column[String]("tunnus")
    def kunta = column[Long]("kunta")
    def alue = column[Long]("alue")
    def lohko = column[Long]("lohko")
    def mts = column[Double]("mts")
    def kuvio = column[Double]("kuvio")
    def alakuvio = column[Option[ String ]]("alakuvio")
    def pinta_ala = column[Double]("pinta_ala")
    def ryhma = column[Double]("ryhma")
    def luokka = column[Double]("luokka")
    def aika = column[Option[ String ]]("aika")
    def teksti = column[Option[ String ]]("teksti")

    def ownerId = column[UserId]("owner")
    def owner = foreignKey("PILE_OWNER_FK", ownerId, UsersComponent.users)(_.id)

    def * = (id.?, wkb_geometry, wood_id, tunnus, kunta, alue, lohko, mts, kuvio, alakuvio, pinta_ala,
      ryhma, luokka, aika, teksti, ownerId) <> (Pile.tupled, Pile.unapply)
  }

}

object PilesComponent extends PilesComponent {
  val piles = TableQuery[Piles]
  override val driver = myUtils.MyPostgresDriver
}
