import play.api._
import play.api.libs.json._
import models._

import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.functional.syntax.toInvariantFunctorOps
import com.vividsolutions.jts.geom.Geometry

object Global extends GlobalSettings {
  override def onStart(app: Application) {

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  implicit val jsonUserIdFmt = Json.format[UserId]
  implicit val jsonPileIdFmt = Json.format[PileId]

}
