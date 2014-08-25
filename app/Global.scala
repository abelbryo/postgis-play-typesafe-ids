import play.api._
import play.api.libs.json._
import models._


object Global extends GlobalSettings {
  override def onStart(app: Application) {

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

 implicit val jsonUserId = Json.format[UserId] 
}
