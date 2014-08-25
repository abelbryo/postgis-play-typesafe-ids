package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._

import models._
import repository._

import java.io.File
import java.util.{ List => JList, Map => JMap }

import org.geotools.data.DataStore
import org.geotools.data.shapefile.ShapefileDataStore
import org.geotools.filter.text.cql2.CQL
import org.geotools.filter.text.cql2.CQLException
import org.opengis.filter.Filter

import org.geotools.data.simple.SimpleFeatureCollection
import org.geotools.data.simple.SimpleFeatureIterator
import org.geotools.data.simple.SimpleFeatureSource
import org.geotools.feature.simple.SimpleFeatureTypeImpl
import org.opengis.feature.`type`.AttributeType
import org.opengis.feature.simple.SimpleFeature

import com.vividsolutions.jts.geom.Geometry;
import scala.collection.JavaConversions._

object WoodPileController extends Controller {

  def index = Action { implicit rs =>
    Ok(views.html.upload())
  }

  def geomAsGeoJSON(id: Long) = Action {
    implicit request =>
      val pileJSON = PilesRepository.asGeoJSON(PileId(id)).getOrElse("{response: 404}")
      Ok(pileJSON).as("application/json")
  }

  def geomByOwerIdAsGeoJSON(ownerId: Long) = Action {
    implicit request =>
      val pileJSON = PilesRepository.geomByOwnerId(UserId(ownerId))
      val json = Json.toJson(pileJSON)
      Ok(json)
  }

  def pileDataAsGeoJSON(id: Long) = Action {
    implicit request =>
      val geometry = PilesRepository.asGeoJSON(PileId(id)).getOrElse("{response: 404}")
      val pile = PilesRepository.getPileById(PileId(id))

      val properties = pile match {
        case None => Json.obj("response" -> 404)
        case _ => pile.get.toJSON
      }

      val geojson = Json.obj("type" -> "Feature",
        "geometry" -> geometry,
        "properties" -> properties)

      Ok(geojson)
    // Ok(views.html.pileview(properties, pileJSON))
  }

  def upload = Action(parse.multipartFormData) { implicit request =>
    val uploadedFiles: Seq[MultipartFormData.FilePart[play.api.libs.Files.TemporaryFile]] = request.body.files

    uploadedFiles foreach {
      case MultipartFormData.FilePart(key, filename, contentType, ref) => {
        play.Logger.debug("Filename " + filename)
        val tmpFile = new File("/tmp/shapefiles/")
        if (!tmpFile.exists)
          tmpFile.mkdir()
        ref.moveTo(new File(s"/tmp/shapefiles/$filename"))
      }
      case _ => Redirect(routes.WoodPileController.index).flashing("error" -> "Upload failed.")
    }

    Redirect(routes.WoodPileController.index).flashing("success" -> "Shapefile uploaded")
  }

  def testShapefile = Action { implicit request =>
    val list = new File("/tmp/shapefiles/").listFiles()
    list map { e =>
      play.Logger.debug("Filename --->  " + e.getName())
      if (e.getName.endsWith(".shp")) {
        readShapeFilesAndInsertIntoDB(e)
      }
    }
    Ok("Testing ... shapefiles: Watch the console log ")
  }

  private def readShapeFilesAndInsertIntoDB(file: File) {
    val store = new ShapefileDataStore(file.toURI.toURL())
    val typeName = store.getTypeNames()(0)
    val source: SimpleFeatureSource = store.getFeatureSource(typeName)

    val features: SimpleFeatureCollection = source.getFeatures()
    val ft: SimpleFeatureTypeImpl = source.getSchema().asInstanceOf[SimpleFeatureTypeImpl]

    val attrNameList = for {
      i <- 0 to ft.getAttributeCount() - 1
      attrType = ft.getType(i).getName()
    } yield attrType

    attrNameList map (println)

    val iterator: SimpleFeatureIterator = features.features()
    while (iterator.hasNext()) {
      val feature: SimpleFeature = iterator.next()

      val pile = Pile(None,
        feature.getAttribute(0).asInstanceOf[Geometry],
        Option(feature.getAttribute(attrNameList(1)).toString),
        feature.getAttribute(attrNameList(2)).toString,
        feature.getAttribute(attrNameList(3)).toString,
        feature.getAttribute(attrNameList(4)).toString,
        feature.getAttribute(attrNameList(5)).toString,
        feature.getAttribute(attrNameList(6)).toString,
        feature.getAttribute(attrNameList(7)).toString,
        Option(feature.getAttribute(attrNameList(8)).toString),
        feature.getAttribute(attrNameList(9)).toString,
        feature.getAttribute(attrNameList(10)).toString,
        feature.getAttribute(attrNameList(11)).toString,
        Option(feature.getAttribute(attrNameList(12)).toString),
        Option(feature.getAttribute(attrNameList(13)).toString),
        UserId(1),
        file.getName())

      PilesRepository.create(pile)

      play.Logger.debug("========= END INSERT ===========")

    } // end while
    iterator.close()
  }

  def updateUser = Action { implicit request =>
    val oldUser = UsersRepository.getUserById(UserId(1))
    oldUser.map { old =>
      val newUser = User(old.id, "Haile Gebresillasie", WayId(2))
      UsersRepository.updateUser(newUser)
    }
    Ok("Done")
  }

}
