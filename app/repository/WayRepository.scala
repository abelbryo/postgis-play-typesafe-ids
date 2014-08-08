package repository

import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.impl._

import models._
import models.current.dao.WaysRepository

import play.api.db.slick._
import play.api.Play.current

object QueryMethods {

  def create(row: OsmWay) = {
    DB.withSession {

      implicit session: Session =>

        // repo.create

        WaysRepository save row

    }
  }

}
