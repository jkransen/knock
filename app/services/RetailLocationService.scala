package services

import com.opencsv.CSVParser

import scala.io.Source

class RetailLocationService {

  val csvParser = new CSVParser()

  def loadRetailLocations(): Unit = {

    val lines = Source.fromFile("app/RetailLocationsList").getLines
    val head = csvParser.parseLine(lines.head)
  }
}
