package controllers

import javax.inject._

import play.api.mvc._
import services.RetailLocationService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RetailController @Inject() (retailLocationService: RetailLocationService) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def loadRetailLocations = Action {
    retailLocationService.loadRetailLocations()
    Ok("loaded locations")
  }

  def login(lat: Double, lon: Double) = Action {
    val nearestLocations = retailLocationService.getNearestLocations(lat, lon)
    //Ok(s"nearby $lat $lon: $nearestLocations")
    Ok(views.html.delivery_list("This is the delivery list"))
  }
}
