package models

import play.api.libs.json._

import com.vividsolutions.jts.geom._
import org.joda.time.LocalDateTime

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._
import scala.slick.lifted.Tag

import myUtils.WithMyDriver

case class PileId(id: Long) extends AnyVal with BaseId

object PileId extends IdCompanion[PileId]

case class Pile(id: Option[PileId],
  geometry: Geometry,
  wood_id: Option[String],
  tunnus: String,
  kunta: String,
  alue: String,
  lohko: String,
  mts: String,
  kuvio: String,
  alakuvio: Option[String],
  pinta_ala: String,
  ryhma: String,
  luokka: String,
  aika: Option[String],
  teksti: Option[String],
  ownerId: UserId,
  fileName: String) extends WithId[PileId] {

    // This method intentionally ignores the geometry
    def toJSON() = Json.obj (
      "wood_id" -> wood_id,
      "tunnus" -> tunnus,
      "kunta" -> kunta,
      "alue" -> alue,
      "lohko" -> lohko,
      "mts" -> mts,
      "kuvio" -> kuvio,
      "alakuvio" -> alakuvio,
      "pinta_ala" -> pinta_ala,
      "ryhma" -> ryhma,
      "luokka" -> luokka,
      "aika" -> aika,
      "teksti" -> teksti,
      "ownerId" -> ownerId.id,
      "fileName" -> fileName
    )

}

///   val pile = Pile (None,
///     feature.getAttribute(0).asInstanceOf[ Geometry ],
///     Option(  feature.getAttribute("ID").asInstanceOf[Long] ),
///     feature.getAttribute("Tunnus").asInstanceOf[String],
///     feature.getAttribute("Kunta").asInstanceOf[Long],
///     feature.getAttribute("Alue").asInstanceOf[Long],
///     feature.getAttribute("Lohko").asInstanceOf[Long],
///     feature.getAttribute("Mts").asInstanceOf[Long],
///     feature.getAttribute("Kuvio").asInstanceOf[Long],
///     Option( feature.getAttribute("Alakuvio").asInstanceOf[String] ),
///     feature.getAttribute("Pinta_ala").asInstanceOf[Double] ,
///     feature.getAttribute("Ryhma").asInstanceOf[Long] ,
///     feature.getAttribute("Luokka").asInstanceOf[Long] ,
///     Option( feature.getAttribute("Aika").asInstanceOf[String] ),
///     Option( feature.getAttribute("Teksti").asInstanceOf[String] ),
///     UserId(1),
///     file.getName()
///   )

trait PilesComponent extends WithMyDriver {
  import driver.simple._

  class Piles(tag: Tag) extends IdTable[PileId, Pile](tag, "WOODS") {
    def geometry = column[Geometry]("the_geom")
    def wood_id = column[Option[String]]("wood_id")
    def tunnus = column[String]("tunnus")
    def kunta = column[String]("kunta")
    def alue = column[String]("alue")
    def lohko = column[String]("lohko")
    def mts = column[String]("mts")
    def kuvio = column[String]("kuvio")
    def alakuvio = column[Option[String]]("alakuvio")
    def pinta_ala = column[String]("pinta_ala")
    def ryhma = column[String]("ryhma")
    def luokka = column[String]("luokka")
    def aika = column[Option[String]]("aika")
    def teksti = column[Option[String]]("teksti")

    def ownerId = column[UserId]("owner")
    def owner = foreignKey("PILE_OWNER_FK", ownerId, UsersComponent.users)(_.id)

    def fileName = column[String]("ORIG_FILENAME")

    def * = (id.?, geometry, wood_id, tunnus, kunta, alue, lohko, mts, kuvio, alakuvio, pinta_ala,
      ryhma, luokka, aika, teksti, ownerId, fileName) <> (Pile.tupled, Pile.unapply)

  }

}

object PilesComponent extends PilesComponent {
  val piles = TableQuery[Piles]
  override val driver = myUtils.MyPostgresDriver
}
