package repository

import models._
import models.current.dao.Piles

import play.api.db.slick.DB
import play.api.Play.current

import org.virtuslab.unicorn.UnicornPlay._
import org.virtuslab.unicorn.UnicornPlay.driver.simple._

import scala.slick.jdbc.meta.MTable

// Important to be able to use the unicorn methods
class PilesRepository extends BaseIdRepository[PileId, Pile, Piles](TableQuery[Piles])

object PilesRepository extends PilesRepository {

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

  def getPileById(id: PileId): Option[Pile] = DB.withSession{
    implicit session: Session =>
      this.findById(id)
  }
}
