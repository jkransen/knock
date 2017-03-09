package services

import javax.inject.Inject

import com.opencsv.CSVParser
import domain.RetailLocation
import play.api.db.Database

import scala.annotation.tailrec
import scala.io.Source

class RetailLocationService @Inject() (db: Database) {

  val csvParser = new CSVParser()
  val numNearest = 1

  var locations: List[RetailLocation] = null

  def getNearestLocations(lat: Double, lon: Double): Map[RetailLocation, Double] = {
    if (locations == null) {
      loadRetailLocations()
    }
    getNearestLocationsRec(lat, lon, Map(), locations)
  }

  @tailrec
  private def getNearestLocationsRec(lat: Double, lon: Double, nearest: Map[RetailLocation, Double], remainingLocations: List[RetailLocation]): Map[RetailLocation, Double] = {
    if (remainingLocations.isEmpty) {
      nearest
    } else {
      val nextLocation = remainingLocations.head
      val nextDistance = distance(lat, lon, nextLocation.lat, nextLocation.lon)
      getNearestLocationsRec(lat, lon, keepNearest(nearest + (nextLocation -> nextDistance)), remainingLocations.tail)
    }
  }

  private def distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {
    math.sqrt(math.pow(math.abs(lat1 - lat2), 2) + math.pow(math.abs(lon1 - lon2), 2))
  }

  private def keepNearest(nearestSoFar: Map[RetailLocation, Double]): Map[RetailLocation, Double] = {
    if (nearestSoFar.size <= numNearest) {
      nearestSoFar
    } else {
      val maxDistance = nearestSoFar.values.fold(0: Double) { (a: Double, b: Double) => math.max(a, b) }
      nearestSoFar.collect {
        case (location, distance) if (location.isActive && distance < maxDistance) => (location, distance)
      }
    }
  }

  def loadRetailLocations(): Unit = {
    val lines = Source.fromFile("app/RetailLocationsList.csv").getLines.toStream
    val head = csvParser.parseLine(lines.head)
    val locations = loadLines(head, lines.tail, List())
    locations.foreach(println(_))
    this.locations = locations
  }

  @tailrec
  private def loadLines(head: Array[String], lines: Stream[String], intermediate: List[RetailLocation]): List[RetailLocation] = {
    if (lines.isEmpty) {
      intermediate
    } else {
      val nextLine: Array[String] = csvParser.parseLine(lines.head)
      val rawData: Map[String, String] = head.zip(nextLine).toMap
      val nextLocation = parseLocation(rawData)
      loadLines(head, lines.tail, nextLocation :: intermediate)
    }
  }

  def parseLocation(rawData: Map[String, String]): RetailLocation = {
    RetailLocation(rawData("Retailer Location Name"),
      rawData("Lattitude").replace(',', '.').toDouble,
      rawData("Longitude").replace(',', '.').toDouble,
      rawData("Network Id"),rawData("Retailer LocId"),
      rawData("Agency #"),rawData("Activation Start Date"),
      rawData("Activation End Date"),rawData("Vacation Start1"),
      rawData("Vacation End1"),rawData("Vacation Start2"),
      rawData("Vacation End2"),rawData("Vacation Start3"),
      rawData("Vacation End3"),rawData("Vacation Start4"),
      rawData("Vacation End4"),rawData("Holiday1"),
      rawData("Holiday2"),rawData("Holiday3"),
      rawData("Holiday4"),rawData("Location Address"),
      rawData("Location City"),rawData("Location Region"),
      rawData("Location Postalcode"),rawData("Location Phone"),
      rawData("Location Email"),rawData("Location Direction"),
      rawData("Working Days"),rawData("Opening Hr Day1"),
      rawData("Opening Hr Day2"),rawData("Opening Hr Day3"),
      rawData("Opening Hr Day4"),rawData("Opening Hr Day5"),
      rawData("Opening Hr Day6"),rawData("Opening Hr Day7"),
      rawData("Opening Hrs Holiday"),rawData("Is Active").equals("Y"),
      rawData("Last Update"),rawData("Unique Key"),
      rawData("Location Address2"))
  }
}
