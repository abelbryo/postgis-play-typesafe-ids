package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

import models._
import repository._

import java.io._
import java.util._

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

  def testShapefile = Action {implicit request => 
    val dir = new File("/tmp/shapefiles/")
    val list = dir.listFiles()

    list map { e =>
      play.Logger.debug("Filename --->  " + e.getName())
      if ( e.getName() == "polygon.shp"){
        readShapeFilesAndInsertIntoDB(e)
      }
    }


    Ok("Testing ... shapefiles: Watch the console log ")
  }

  def readShapeFilesAndInsertIntoDB(file: File) = {
    val store = new ShapefileDataStore(file.toURI.toURL())
    val typeName = store.getTypeNames()(0)
    val source: SimpleFeatureSource = store.getFeatureSource(typeName)

    val features: SimpleFeatureCollection = source.getFeatures()
    val ft: SimpleFeatureTypeImpl = source.getSchema().asInstanceOf[SimpleFeatureTypeImpl]

    val iterator: SimpleFeatureIterator = features.features()
    while(iterator.hasNext()){
      val feature: SimpleFeature = iterator.next()

      feature.getAttributes().foreach {
        e => println(e)
      }
    }





    store

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
