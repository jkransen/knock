package services

import com.opencsv.CSVParser

class RetailLocationService {

  val csvParser = new CSVParser()

  def loadRetailLocations(): Unit = {

    val head = csvParser.parseLine("")
  }
}
