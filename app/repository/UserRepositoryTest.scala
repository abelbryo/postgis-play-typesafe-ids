package repository

//import models._
//import repository.current.dao.driver.simple.WaysRepository

//object Queries {

//  def insert = {
//    DB.withSession {
//      
//      implicit session: MyS => 

//      val repo = new WaysRepository
//      repo.create

//      val carr: Array[Coordinate] = Array(new Coordinate(10, 10), new Coordinate(20, 20), new Coordinate(40, 40))
//      val cs = new CoordinateArraySequence(carr)

//      val gf = new GeometryFactory(new PrecisionModel(), 4326)

//      val line = new LineString(cs, gf)
//      val way = OsmWay(None, line )

//      val wayId = repo save way

//    }
//  }
//}

